#ifndef ASSIGNMENT_3_JXSERVER_FINAL_SUBMISSION_SERVER_H
#define ASSIGNMENT_3_JXSERVER_FINAL_SUBMISSION_SERVER_H
#endif //ASSIGNMENT_3_JXSERVER_FINAL_SUBMISSION_SERVER_H

#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <signal.h>
#include<sys/types.h>
#include <netinet/in.h>
#include <math.h>
#include <dirent.h>
#include <byteswap.h>
#include <endian.h>
#include <sys/stat.h>
#include <inttypes.h>


typedef struct unzip{
    int length;
    int *code;
}Unzip;


// print error message
void error(char *msg){
    perror(msg);
    exit(-1);
}


// send error message
void error_functionality(int connect_fd)
{
    uint8_t response[9] = {0xf0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    send(connect_fd, response, 9, 0);
}


// create server socket
int create_server_socket()
{
    int s = socket(AF_INET, SOCK_STREAM, 0);
    if(s == -1)
        error("Can't open socket");

    return s;
}


// bind socket to port and set port reusable
void bind_to_port(int socket, uint32_t ip_address, uint16_t port)
{
    struct sockaddr_in net;
    net.sin_family = AF_INET;
    net.sin_port = port;
    net.sin_addr.s_addr = ip_address;
    int reuse = 1;
    if(setsockopt(socket, SOL_SOCKET, SO_REUSEADDR, (char *)&reuse, sizeof(int)) == -1)
        error("Can't set ip address reusable again");
    if(bind(socket, (struct sockaddr *) &net, sizeof(net)) == -1)
        error("Can't bind to the socket");
}


// get single bit in the array
uint8_t get_bit(uint8_t * array, int index)
{
    index = index + 1;
    int location = 0;
    int reminder = (index ) % 8;
    if((index % 8) > 0){
        location = index / 8;
    }else{
        location = index / 8 -1;
    }
    uint8_t to_return;
    if(reminder > 0){
        to_return = (array[location] >> (8 - reminder)) & 0x01;
    }else{
        to_return = array[location] & 0x01;
    }
    return to_return;
}


// set single bit in the array
void set_bit(uint8_t *array, int index)
{
    index = index + 1;
    int location = 0;
    int reminder = (index) % 8;
    if((index % 8) > 0){
        location = index / 8;
    }else{
        location = index/ 8 - 1;
    }
    uint8_t flag = 1;
    if(reminder > 0){
        flag = flag << (8 - reminder);
        array[location] = array[location] | flag;
    }else{
        array[location] = array[location] | flag;
    }
}


// count bit uncompressed_request_length of the code
int count_length(uint8_t * array, int index)
{
    uint8_t length_part = 0x00;
    int p = 0;
    for(int i = index; i < index + 8; i++)
    {
        p++;
        uint8_t part = get_bit(array, i) << (8 - (p));
        length_part = length_part | part;
    }
    p = 0;

    int total_length = length_part;
    return total_length;
}


// to generate the compressed response
void construct_and_send_compressed_response(uint8_t* uncompressed_response, Unzip* lists, int uncompressed_payload_length, uint8_t response_type, int connect_fd)
{
    int bit_total_length = 0;
    int *bit_total = calloc(1, sizeof(int));
    int count = 0;

    // generate a bit array use int 1, 0 to represent single bit
    for (int i = 0; i < uncompressed_payload_length; ++i) {
        unsigned int index = uncompressed_response[9 + i];
        bit_total_length += lists[index].length;
        void* new_bit_total = realloc(bit_total, bit_total_length* sizeof(int));
        if(!new_bit_total)
            error("realloc fail");
        bit_total = new_bit_total;
        for (int j = 0; j < lists[index].length; ++j) {
            bit_total[count] = lists[index].code[j];
            count += 1;
        }
    }
    unsigned int byte_length = 0;
    unsigned int reminder = bit_total_length % 8;

    // calculate the byte length
    if(reminder > 0)
    {
        byte_length = bit_total_length / 8 + 1;
    } else{
        byte_length = bit_total_length / 8;
    }

    uint8_t *compressed_payload = calloc(byte_length, sizeof(uint8_t));

    // set each bit in the compressed payload
    for(int i = 0; i < bit_total_length; i++)
    {
        if(bit_total[i] == 1)
            set_bit(compressed_payload, i);
    }

    uint8_t *compressed_response = calloc(9+byte_length + 1, sizeof(uint8_t));
    compressed_response[0] = response_type | 0x08;
    uint64_t byte_length_hex = (uint64_t) (byte_length+1);
    uint8_t padding_bit_hex;

    //calculate the padding bit
    if(reminder > 0)
    {
        padding_bit_hex = (uint8_t) (8 - reminder);
    }else{
        padding_bit_hex = (uint8_t) reminder;
    }
    byte_length_hex = htobe64(byte_length_hex);
    memcpy(compressed_response + 1, &byte_length_hex, sizeof(uint64_t));
    for (int k = 0; k < byte_length; ++k) {
        compressed_response[k+9] = compressed_payload[k];
    }
    compressed_response[byte_length + 9] = padding_bit_hex;
    send(connect_fd, compressed_response, 9+byte_length+1, 0);

    free(bit_total);
    free(compressed_payload);
    free(compressed_response);

}


// send retrieve file response by requirements
int send_retrieve_file_response(uint8_t* request_payload, int uncompressed_request_length, char* target_dir, int connect_fd, int configure_length, uint8_t compression_needed, Unzip* lists)
{
    // generate the session id, offset , length of requested, file name
    uint32_t session_id;
    uint64_t offset_hex;
    uint64_t length_of_requested_file_hex;
    char* file_name = calloc(uncompressed_request_length - 20, sizeof(char));
    memcpy(&session_id, request_payload, sizeof(session_id));
    memcpy(&offset_hex, request_payload+4, sizeof(offset_hex));
    memcpy(&length_of_requested_file_hex, request_payload + 12, sizeof(uint64_t));
    offset_hex = htobe64(offset_hex);
    length_of_requested_file_hex = htobe64(length_of_requested_file_hex);
    file_name = memcpy(file_name, request_payload + 20, uncompressed_request_length - 20);
    file_name[uncompressed_request_length - 20 - 1] = '\0';
    DIR *folder;
    struct dirent* entry;
    folder = opendir(target_dir);
    if(folder == NULL)
        error("Couldn't open the directory");
    unsigned int offset = offset_hex;
    unsigned int request_length = length_of_requested_file_hex;
    char* content = calloc(request_length, sizeof(char));
    int bad_range = 0;
    int non_exist = 1;

    // find the target file and read content
    while((entry = readdir(folder)))
    {
        if(entry->d_type == 8)
        {
            if(strcmp(file_name, entry->d_name) == 0)
            {
                // generate the absolute path of the target file
                char path[100];
                strcpy(path, target_dir);
                path[configure_length - 6] = '/';
                strcpy(path + configure_length - 6 + 1, file_name);
                FILE *target_file = fopen(path, "rb");
                if(target_file == NULL)
                    error("Can't open the file");
                non_exist = 0;
                fseek(target_file, 0, SEEK_END);
                unsigned int total_length = ftell(target_file);
                fseek(target_file, 0, SEEK_SET);
                if((offset + request_length) > total_length)
                {
                    bad_range = 1;
                    break;
                }
                fseek(target_file, offset, SEEK_SET);
                fread(content, 1, request_length, target_file);
                fclose(target_file);
            }
        }
    }
    closedir(folder);

    // to check if the target file in bad range or non exist
    if(bad_range == 1 || non_exist == 1)
    {
        free(file_name);
        free(content);
        free(request_payload);
        error_functionality(connect_fd);
        return -2;
    }

    // generate the uncompressed payload
    uint8_t *uncompressed_response = calloc(1+8+20+request_length, sizeof(uint8_t));
    uncompressed_response[0] = 0x70;
    unsigned int payload_length = 20 + request_length;
    uint64_t uncompressed_payload_length_hex = payload_length;
    uncompressed_payload_length_hex = htobe64(uncompressed_payload_length_hex);
    memcpy(uncompressed_response + 1, &uncompressed_payload_length_hex, sizeof(uint64_t));
    memcpy(uncompressed_response + 9, &session_id, sizeof(uint32_t));
    offset_hex = htobe64(offset_hex);
    memcpy(uncompressed_response + 13, &offset_hex, sizeof(uint64_t));
    length_of_requested_file_hex = htobe64(length_of_requested_file_hex);
    memcpy(uncompressed_response + 21, &length_of_requested_file_hex, sizeof(uint64_t));
    memcpy(uncompressed_response + 29, content, request_length);

    free(file_name);
    free(content);
    free(request_payload);

    if(compression_needed == 0x01)
    {
        construct_and_send_compressed_response(uncompressed_response, lists, 20+request_length, 0x70, connect_fd);
    } else{
        send(connect_fd, uncompressed_response , 29 + request_length, 0);
    }


    free(uncompressed_response);

    return 1;
}


// decompress the compressed payload
uint8_t * generate_decompressed_payload(int compressed_length, uint8_t * compressed_payload, Unzip* lists, uint8_t* decompressed_payload, int* decompressed_length)
{
    // to save the decompression payload
    // decompressed_payload = NULL;
    // decompressed_length = 0;
    int size_of_current_possible_indexs = 256;
    int *possible_indexs = calloc(256, sizeof(int));
    int *new_possible_indexs = NULL;
    int size_of_new_possible_indexs = 0;

    // initialize the all current possible index for first time
    for(int i = 0; i < 256; i++)
        possible_indexs[i] = i;

    // calculate the padding number in the compressed payload
    unsigned int padding_number = compressed_payload[compressed_length - 1];

    // count is for counting the index of the bit in corresponding compressed byte
    // when find out one decompression code we set reset into 1
    // when reset come to 1 we set conut into 0
    int count = 0;
    int reset = 0;

    // travse the compressed payload bit by bit to until we find
    // possbile index of the compressed code
    for(int i = 0; i < (compressed_length-1)*8 - padding_number; i++){
        uint8_t current_bit = get_bit(compressed_payload, i);
        unsigned int num =  current_bit;
        for(int j = 0 ; j < size_of_current_possible_indexs; j++)
        {
            // If the bit in compressed payload equals to the bit in compression dictionary,
            // we will add it into the arrray of new_possible_indexs.
            // The array of new_possible_indexs will change each time, until we find the exact one
            if(lists[possible_indexs[j]].code[count] == num )
            {
                // generate the new array of new_possible_indexs
                if(new_possible_indexs == NULL)
                {
                    new_possible_indexs = calloc(1, sizeof(int));
                    new_possible_indexs[0] = possible_indexs[j];
                    size_of_new_possible_indexs += 1;
                }else{
                    new_possible_indexs = realloc(new_possible_indexs, sizeof(int)*(size_of_new_possible_indexs + 1));
                    new_possible_indexs[size_of_new_possible_indexs] = possible_indexs[j];
                    size_of_new_possible_indexs += 1;
                }

                // We have fonud the corresponding byte
                if((count + 1) == lists[possible_indexs[j]].length)
                {
                    if(decompressed_payload == NULL)
                    {
                        decompressed_payload = calloc(1, sizeof(uint8_t));
                        unsigned int index = possible_indexs[j];
                        uint8_t code = (uint8_t) index;
                        decompressed_payload[0] = code;
                        (*decompressed_length) += 1;
                    }else{
                        decompressed_payload = realloc(decompressed_payload, sizeof(uint8_t)*((*decompressed_length)+1));
                        unsigned int index = possible_indexs[j];
                        uint8_t code = (uint8_t) index;
                        decompressed_payload[*decompressed_length] = code;
                        (*decompressed_length) += 1;
                    }
                    // reset the count for next compressed code
                    reset = 1;
                    if(i == (compressed_length-1)*8 - padding_number - 1)
                    {
                        free(new_possible_indexs);
                    }else{
                        new_possible_indexs = realloc(new_possible_indexs, sizeof(int)*256);
                        for(int k = 0; k < 256; k++)
                            new_possible_indexs[k] = k;
                        size_of_new_possible_indexs = 256;
                    }
                }

            }
        }

        if(reset == 1){
            count = 0;
        }else{
            count += 1;
        }
        reset = 0;
        free(possible_indexs);
        possible_indexs = new_possible_indexs;
        new_possible_indexs = NULL;
        size_of_current_possible_indexs = size_of_new_possible_indexs;
        size_of_new_possible_indexs = 0;
    }

    return decompressed_payload;
}