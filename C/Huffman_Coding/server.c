#include <stdio.h>
#include "server.h"
#include <fcntl.h>



int main(int argc, char *argv[]){
    if(argc != 2)
        error("Arguments Error");

    int pid;                                // save child pid
    int listen_fd;                          // save server listener file descriptor
    int connect_fd;                         // save server connected file descriptor
    FILE * fp;                              // open the configure file
    int communicate_fd[2];                  // communicate between parent and child

    // construct the compression dictionary
    uint8_t *dict;
    Unzip lists[256];
    FILE *dictionary = fopen("compression.dict", "rb");
    if(dictionary == NULL)
        error("Error no such file");
    fseek(dictionary, 0, SEEK_END);
    int dict_length = ftell(dictionary);
    fseek(dictionary, 0, SEEK_SET);
    dict = calloc(dict_length, sizeof(uint8_t));
    fread(dict, 1, dict_length, dictionary);

    int accumulate = 0;
    for(int i = 0; i < 256; i++){
        lists[i].length  = count_length(dict, accumulate);
//        printf("Length for %d %d\n", i, lists[i].length);
        accumulate += 8;
        lists[i].code = calloc(lists[i].length, sizeof(int));
        for(int j = 0; j < lists[i].length; j++){
            int num = get_bit(dict, accumulate + j);
            if(num > 0)
            {
                // current bit is 0x01
                lists[i].code[j] = 1;
            }else{
                // current bit is 0x00
                lists[i].code[j] = 0;
            }
        }
        accumulate += lists[i].length;
    }

    // to save session_id has been transfered
    uint32_t* session_id_lists = NULL;
    int size_of_session_id_lists = 0;

    // Create file number for listening
    listen_fd = create_server_socket();

    // read ip, port , directory
    fp = fopen(argv[1], "rb");
    if(fp == NULL)
        error("Path do not exist");
    fseek(fp, 0, SEEK_END);
    int configure_length = ftell(fp);
    fseek(fp, 0, SEEK_SET);
    // ip address
    uint32_t inaddr;
    fread(&inaddr, sizeof(inaddr), 1, fp);
    // port
    uint16_t port;
    fread(&port, sizeof(port), 1, fp);
    // target directory
    char target_dir[100];
    fread(target_dir, 1, configure_length-6, fp);
    target_dir[configure_length - 6] = '\0';

    bind_to_port(listen_fd, inaddr, port);
    if(listen(listen_fd, 10) == -1)
        error("Can't listen");

    // create communication pipe and set the mode into non-blocking
    pipe(communicate_fd);
    fcntl( communicate_fd[0], F_SETFL, fcntl(communicate_fd[0], F_GETFL) | O_NONBLOCK);
    for (;;)
    {
        if((connect_fd = accept(listen_fd, (struct sockaddr*)NULL, NULL)) < 0)
            error("Accept error");

        // to handle multiple connection
        if((pid = fork()) == 0)
        {
            close(listen_fd);
            close(communicate_fd[0]);

            while(1){
                uint8_t request[9];
                recv(connect_fd, request, 9, 0);

                // bit masking to get the first 4 type digit
                uint8_t type = (request[0] >> 4) & 0x0f;
                uint8_t compressed = (request[0] >> 3) & 0x01;
                uint8_t compression_needed  = (request[0] >> 2) & 0x01;

                // calculate length of variable payload
                int length = 0;
                for (int i = 1; i < 9; ++i) {
                    if(request[i] != 0x00){
                        unsigned int count = request[i];
                        length += count*pow(2, (8 - i)*8);
                    }
                }

                // echo functionality
                if(type == 0x0)
                {
                    if(length > 0){
                        // receive the payload
                        uint8_t *payload = calloc(length, sizeof(uint8_t));
                        recv(connect_fd, payload, length, 0);
                        // generate uncompressed payload
                        uint8_t *response = calloc(length + 9, sizeof(uint8_t));
                        response[0] = 0x10;
                        if(compressed == 0x01)
                            response[0] = response[0] | 0x08;
                        for (int i = 1; i < 9; ++i) {
                            response[i] = request[i];
                        }
                        for (int j = 0; j < length; ++j) {
                            response[j+9] = payload[j];
                        }

                        if(compression_needed == 0x01)
                        {
                            if(compressed != 0x01)
                            {
                                construct_and_send_compressed_response(response, lists, length, 0x10, connect_fd);
                            }else{
                                send(connect_fd, response, 9+length, 0);
                            }
                        }else{
                            send(connect_fd, response, 9+length, 0);
                        }
                        free(payload);
                        free(response);
                    }else{
                        request[0] = 0x10;
                        send(connect_fd, request, 9, 0);
                    }

                }
                else if(type == 0x2)
                {
                    // Directory listing
                    DIR* folder;
                    struct dirent *entry;
                    folder = opendir(target_dir);
                    if(folder == NULL){
                        error("Couldn't open the dirstory");
                    }

                    uint64_t payload_len_hex = 0;
                    int payload_len = 0;
                    while( (entry=readdir(folder)) )
                    {
                        if(entry->d_type == 8){
                            payload_len += (strlen(entry->d_name) + 1);
                            payload_len_hex += (strlen(entry->d_name) + 1);
                            //printf("Length %3d %hhx: %s, %d\n", payload_len, payload_len_hex ,entry->d_name, strlen(entry->d_name));
                        }
                    }
                    payload_len_hex = htobe64(payload_len_hex);
                    uint8_t *response = calloc(9+payload_len, sizeof(uint8_t));
                    response[0] = 0x30;
                    memcpy(response + 1, &payload_len_hex, sizeof(payload_len_hex));
                    rewinddir(folder);
                    int have_copied = 9;
                    while( (entry=readdir(folder)) )
                    {
                        if(entry->d_type == 8)
                        {
                            memcpy(response + have_copied, entry->d_name, strlen(entry->d_name));
                            have_copied += strlen(entry->d_name);
                            response[have_copied] = 0x00;
                            have_copied += 1;
                        }
                    }
                    closedir(folder);

                    if(compression_needed == 0x01){
                        construct_and_send_compressed_response(response, lists, payload_len, 0x30, connect_fd);
                    }else{
                        send(connect_fd, response, 9+payload_len, 0);
                    }
                    free(response);

                }
                else if(type == 0x4)
                {
                    // File size query
                    DIR *folder;
                    struct dirent* entry;
                    struct stat file_stat;
                    folder = opendir(target_dir);
                    if(folder == NULL){
                        error("Couldn't open the directory");
                    }
                    char *file_name = calloc(length, sizeof(uint8_t));
                    recv(connect_fd, file_name, length, 0);
                    file_name[length - 1] = '\0';
                    unsigned int file_length = 0;
                    uint64_t file_length_hex;
                    while((entry = readdir(folder)))
                    {
                        // the type of the content is a normal file
                        if(entry->d_type == 8)
                        {
                            if(strcmp(file_name, entry->d_name) == 0)
                            {
                                // generate the absolute path of the target file
                                char path[100];
                                strcpy(path, target_dir);
                                path[configure_length - 6] = '/';
                                strcat(path, file_name);
                                if(stat(path, &file_stat) < 0)
                                    error("Fail to inspect the file state");

                                file_length = file_stat.st_size;
                            }
                        }
                    }
                    closedir(folder);
                    free(file_name);
                    file_length_hex = file_length;
                    file_length_hex = htobe64(file_length_hex);
                    uint8_t *response = calloc(9 + 8, sizeof(uint8_t));
                    response[0] = 0x50;
                    response[8] = 0x08;
                    memcpy(response + 9, &file_length_hex, sizeof(file_length_hex));
                    if(file_length > 0)
                    {
                        if(compression_needed == 0x01)
                        {
                            construct_and_send_compressed_response(response, lists, 8, 0x50, connect_fd);
                        }else{
                            send(connect_fd, response, 9 + 8, 0);
                        }
                        free(response);
                    }else{
                        free(response);
                        error_functionality(connect_fd);
                        break;
                    }

                }
                else if(type == 0x6)
                {

                    // Retrieve file
                    uint8_t *request_payload = calloc(length, sizeof(uint8_t));
                    if(compressed != 0x01)
                    {
                        recv(connect_fd, request_payload, length, 0);
                        uint32_t session_id;
                        int exist = 0;
                        memcpy(&session_id, request_payload, sizeof(session_id));

                        // search if this session id have been transfer
                        for (int i = 0; i < size_of_session_id_lists; ++i) {
                            if(session_id == session_id_lists[i])
                            {
                                printf("Exists\n");
                                exist = 1;
                            }
                        }

                        if(exist == 0)
                        {
                            write(communicate_fd[1], &session_id, sizeof(uint32_t));

                            // this session id have not been transfer
                            int state = send_retrieve_file_response(request_payload, length, target_dir, connect_fd, configure_length, compression_needed, lists);

                            // bad range or file do not exist;
                            if(state == -2)
                                break;
                        }else{
                            printf("Send NUll\n");
                            uint8_t null_response[9];
                            null_response[0] = 0x70;
                            for (int i = 1; i < 9; ++i) {
                                null_response[i] = 0x00;
                            }
                            send(connect_fd, null_response, 9, 0);
                            break;
                        }

                    }else{
                        recv(connect_fd, request_payload, length, 0);
                        int compressed_length = length;
                        int decompressed_length = 0;
                        uint8_t * decompressed_request_payload = NULL;
                        decompressed_request_payload = generate_decompressed_payload(compressed_length, request_payload, lists, decompressed_request_payload, &decompressed_length);
                        free(request_payload);
                        uint32_t session_id;
                        int exist = 0;
                        memcpy(&session_id, decompressed_request_payload, sizeof(session_id));
                        // search if this session id have been transfer
                        for (int i = 0; i < size_of_session_id_lists; ++i) {
                            if(session_id == session_id_lists[i])
                            {
                                printf("Exists\n");
                                exist = 1;
                            }
                        }

                        if(exist == 0)
                        {

                            write(communicate_fd[1], &session_id, sizeof(uint32_t));
                            // this session id have not been transfer
                            int state = send_retrieve_file_response(decompressed_request_payload, decompressed_length, target_dir, connect_fd, configure_length, compression_needed, lists);
                            // bad range or file do not exist;
                            if(state == -2)
                                break;

                        }else{
                            printf("Send NUll\n");
                            uint8_t null_response[9];
                            null_response[0] = 0x70;
                            for (int i = 1; i < 9; ++i) {
                                null_response[i] = 0x00;
                            }
                            send(connect_fd, null_response, 9, 0);
                            break;
                        }

                    }
                }
                else if(type == 0x8)
                {
                    // Shutdown command to implement
                    break;
                }
                else{
                    error_functionality(connect_fd);
                    break;

                }


            }
            if(session_id_lists != NULL)
                free(session_id_lists);
            for (int l = 0; l < 256; ++l) {
                free(lists[l].code);
            }
            free(dict);
            close(connect_fd);
            exit(0);

        }
        close(connect_fd);
        close(communicate_fd[1]);
        uint32_t session_id;
        if(read(communicate_fd[0], &session_id, sizeof(uint32_t)) > 0)
        {
            // add it to session id lists
            printf("have added\n");
            if(size_of_session_id_lists == 0)
            {
                session_id_lists = calloc(1, sizeof(uint32_t));
                session_id_lists[0] = session_id;
                size_of_session_id_lists += 1;
            }else{
                session_id_lists = realloc(session_id_lists , sizeof(uint32_t)*(size_of_session_id_lists + 1));
                session_id_lists[size_of_session_id_lists] = session_id;
                size_of_session_id_lists += 1;
            }
        }
    }

    return 0;
}