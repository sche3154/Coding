#include "svc.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

typedef unsigned char byte;

typedef struct tracked{
    /// status is to record working status all status in the beginning is 0
    /// Three Examples:
    /// 1. svc_add
    ///     status - > 1;
    ///    svc_rm
    ///      remove it immidiately;
    /// 2.  svc_rm
    ///       status -> -1 (remove it after commit);
    /// 3. svc_rm
    ///       status -> -1
    ///    svc_add
    ///       status -> 0 (nothing happened, keep saving it)
    int status;
    char* file_path; // original file path
    int old_hash;
    int hash_value;
}Tracked_file;

typedef struct Commit{
    char* commit_id;
    char* commit_message;
    int size_of_files;
    Tracked_file *saved_files;  // copy of all the tracked_files
    struct Commit* previous;
    struct Commit* next;


}Commit;

typedef struct Branch{
    char* name;
    int num_tracked;
    Tracked_file* file_list; // tracked files
    Commit* current_commit;
}Branch;

typedef struct helper{
    Branch* active_branch;
    Branch* Branch_list;
    int size_of_branch;
}Helper;

void *svc_init(void) {
    Helper *helper = calloc(1, sizeof(Helper));
    helper->size_of_branch = 1;
    helper->Branch_list = calloc(1, sizeof(Branch));
    helper->Branch_list[0].name = strdup("master");
    helper->Branch_list[0].current_commit = NULL;
    helper->Branch_list[0].num_tracked = 0;
    helper->active_branch = &(helper->Branch_list[0]);
    return helper;
}

void cleanup(void *helper) {
    // TODO: Implement
    Helper * helper1 = (Helper *)helper;
    for (int i = 0; i < helper1->size_of_branch; i++) {
        free(helper1->Branch_list[i].name);
        if(helper1->Branch_list[i].num_tracked > 0){
            for (int j = 0; j < helper1->Branch_list[i].num_tracked; j++) {
                free(helper1->Branch_list[i].file_list[j].file_path);
            }
            free(helper1->Branch_list[i].file_list);
        }
        Commit* current = helper1->Branch_list[i].current_commit;
        // free all the commit by previous attributes
        while(current != NULL){
            Commit* temp  = current;
            current = current->previous;
            for (int j = 0; j < temp->size_of_files; ++j) {
                free(temp->saved_files[j].file_path);
            }
            free(temp->commit_id);
            free(temp->commit_message);
            free(temp->saved_files);
            free(temp);
        }
    }
    free(helper1->Branch_list);
    free(helper1);
}



int hash_file(void *helper, char *file_path) {

    int hash_value = 0 ;// set the default hash_value
    if(file_path == NULL){
        return  -1;
    }// file path is null
    FILE *fp = fopen(file_path, "rb");
    // file do not exist
    if(fp == NULL ){
        return  -2;
    }
    else{
        byte no_sign; // to save unsigned byte
        int length = strlen(file_path);
        // compute the hash in file_path
        for(int i = 0; i < length; i++){
            if(file_path[i] != '\0'){
                no_sign = file_path[i]; // convert char into byte
                hash_value = (hash_value + no_sign) % 1000;
            }
        }

        fseek(fp, 0, SEEK_END); // set the file pointer to the end of the file
        int size_of_file = ftell(fp);
        fseek(fp, 0, SEEK_SET);// set the file pointer to the beginning

        // compute the hash in file contents
        for (int j = 0; j < size_of_file; j++) {
            fread(&no_sign, 1, 1, fp);
            hash_value = (hash_value + no_sign) % 2000000000;
        }
    }
    return hash_value;
}

// qsort function
int compare(const void * a, const void * b){
    const char **ia = (const char **)a;
    const char **ib = (const char **)b;
    // compare the lower form of first character of file name
    int na = tolower(**ia);
    int nb = tolower(**ib);

    // EX : 'a' & 'A'
    if(na - nb == 0){
        return strcmp(*ia, *ib);
    }
    return na - nb ;

}


char *svc_commit(void *helper, char *message) {
    Helper *helper1 = (Helper *)helper;
    if(message == NULL){
        return NULL;
    }

    // remove all the files by manually removed
    for (int a = 0; a < helper1->active_branch->num_tracked; a++) {
        if(helper1->active_branch->file_list[a].status == 1){
            if(hash_file(helper1, helper1->active_branch->file_list[a].file_path) == -2){
                svc_rm(helper1, helper1->active_branch->file_list[a].file_path);
            }
        }
    }

    char *name[helper1->active_branch->num_tracked];

    for (int i = 0; i < helper1->active_branch->num_tracked; ++i) {
        name[i] = helper1->active_branch->file_list[i].file_path;
    }

    // sort all the name by alphabet order
    qsort(name, helper1->active_branch->num_tracked, sizeof(char*), compare);

    if(helper1->active_branch->num_tracked == 0){
        return NULL;
    }
    if(helper1->active_branch->current_commit == NULL){
        int id = 0;
        byte c;
        int i = 0;

        while(message[i]){
            c = message[i];
            id = (id + c) % 1000;
            i++;
        }

        // compute commit id according changes
        for (int i = 0; i < helper1->active_branch->num_tracked; ++i) {
            for (int k = 0; k < helper1->active_branch->num_tracked; ++k) {
                if(strcmp(name[i], helper1->active_branch->file_list[k].file_path) == 0){
                    int hash_value = hash_file(helper1,  name[i]);
                    if(hash_value == -2){
                        helper1->active_branch->file_list[k].status = -1;
                    }
                    if(helper1->active_branch->file_list[k].status == 1){ // added in this branch
                        id = id + 376591;
                        for (int j = 0; j < strlen(name[i]); ++j) {
                            if(name[i][j] != '\0'){
                                c = name[i][j];
                                id = (id * (c % 37)) % 15485863 + 1;
                            }
                        }
                        break;
                    }else if(helper1->active_branch->file_list[k].status == -1){ //remove and will not be seen after commit
                        id = id +85973;
                        for (int j = 0; j < strlen(name[i]); ++j) {
                            if(name[i][j] != '\0'){
                                c = name[i][j];
                                id = (id * (c % 37)) % 15485863 + 1;
                            }
                        }
                        break;
                    }
                    if((hash_value != helper1->active_branch->current_commit->saved_files[k].hash_value) && (hash_value != -2)){
                        id = id + 9573681;
                        helper1->active_branch->current_commit->saved_files[k].old_hash = helper1->active_branch->current_commit->saved_files[k].hash_value;
                        helper1->active_branch->current_commit->saved_files[k].hash_value = hash_value;
                        for (int j = 0; j < strlen(name[i]); ++j) {
                            if(name[i][j] != '\0'){
                                c = name[i][j];
                                id = (id * (c % 37)) % 15485863 + 1;
                            }
                        }
                        break;
                    }
                }
            }
        }// Finish

        //Create commit
        helper1->active_branch->current_commit = calloc(1, sizeof(Commit));
        helper1->active_branch->current_commit->previous = NULL;
        helper1->active_branch->current_commit->commit_message = strdup(message);
        helper1->active_branch->current_commit->size_of_files = helper1->active_branch->num_tracked;
        helper1->active_branch->current_commit->commit_id = calloc(7, sizeof(char));
        sprintf(helper1->active_branch->current_commit->commit_id, "%06x", id);
        // malloc size for copied tracked files
        helper1->active_branch->current_commit->saved_files = calloc(helper1->active_branch->current_commit->size_of_files, sizeof(Tracked_file));
        for (int k = 0; k < helper1->active_branch->current_commit->size_of_files; ++k) {
            helper1->active_branch->current_commit->saved_files[k].file_path = strdup(helper1->active_branch->file_list[k].file_path);
            helper1->active_branch->current_commit->saved_files[k].hash_value = helper1->active_branch->file_list[k].hash_value;
            helper1->active_branch->current_commit->saved_files[k].status = helper1->active_branch->file_list[k].status;
            helper1->active_branch->current_commit->saved_files[k].old_hash = helper1->active_branch->file_list[k].old_hash;
        }
        //Finish Commit

        ///reset the working branch
        int to_removed = 0;
        for (int l = 0; l < helper1->active_branch->num_tracked; ++l) {
            if(helper1->active_branch->file_list[l].status == -1 ){
                to_removed += 1;
            }
        }
        //  remove all files status equal to -1
        for (int m = 0; m < to_removed; ++m) { // remove all the files status == -1
            for (int i = 0; i < helper1->active_branch->num_tracked; ++i) {
                int num_of_files = helper1->active_branch->num_tracked;
                if(helper1->active_branch->file_list[i].status == -1){ // add just now then remove
                    int index_to_remove = i;
                    Tracked_file* copy_array = malloc((num_of_files - 1) * sizeof(Tracked_file));
                    for (int j = 0; j < index_to_remove; ++j) {
                        copy_array[j].status = helper1->active_branch->file_list[j].status;
                        copy_array[j].file_path = strdup(helper1->active_branch->file_list[j].file_path);
                        copy_array[j].hash_value = helper1->active_branch->file_list[j].hash_value;
                        copy_array[j].old_hash = helper1->active_branch->file_list[j].old_hash;
                    }
                    for (int k = index_to_remove; k < num_of_files - 1; k++) {
                        copy_array[k].status = helper1->active_branch->file_list[k+1].status;
                        copy_array[k].file_path = strdup(helper1->active_branch->file_list[k+1].file_path);
                        copy_array[k].hash_value = helper1->active_branch->file_list[k+1].hash_value;
                        copy_array[k].old_hash = helper1->active_branch->file_list[k].old_hash;
                    }
                    for (int j = 0; j < helper1->active_branch->num_tracked; ++j) {
                        free(helper1->active_branch->file_list[j].file_path);
                    }
                    helper1->active_branch->file_list = realloc(helper1->active_branch->file_list, sizeof(Tracked_file)*(num_of_files - 1));
                    for (int l = 0; l < num_of_files - 1; ++l) {
                        helper1->active_branch->file_list[l] = copy_array[l];
                    }
                    free(copy_array);
                    helper1->active_branch->num_tracked -= 1;
                    break;
                }
            }
        }
        // reset all status to 0 again
        for (int n = 0; n < helper1->active_branch->num_tracked; ++n) {
            helper1->active_branch->file_list[n].status = 0;
        }
        ///Finish Reset

    }else{
        int id = 0;
        byte c;
        int i = 0;
        while(message[i]){
            c = message[i];
            id = (id + c) % 1000;
            i++;
        }

        int un_changed = 0; // to see if nothing changes since last commit

        // Compute commit id
        for (int i = 0; i < helper1->active_branch->num_tracked; ++i) {
            for (int k = 0; k < helper1->active_branch->num_tracked; ++k) {
                if(strcmp(name[i], helper1->active_branch->file_list[k].file_path) == 0){
                    int hash_value = hash_file(helper1,  name[i]);
                    if(hash_value == -2){
                        helper1->active_branch->file_list[k].status = -1;
                    }
                    if(helper1->active_branch->file_list[k].status == 1){ // added in this branch version
                        id = id + 376591;
                        for (int j = 0; j < strlen(name[i]); ++j) {
                            if(name[i][j] != '\0'){
                                c = name[i][j];
                                id = (id * (c % 37)) % 15485863 + 1;
                            }
                        }
                        break;
                    }else if(helper1->active_branch->file_list[k].status == -1){ // to be removed
                        id = id +85973;
                        for (int j = 0; j < strlen(name[i]); ++j) {
                            if(name[i][j] != '\0'){
                                c = name[i][j];
                                id = (id * (c % 37)) % 15485863 + 1;
                            }
                        }
                        break;
                    }
                    if((hash_value != helper1->active_branch->current_commit->saved_files[k].hash_value) && (hash_value != -2)){
                        id = id + 9573681;
                        helper1->active_branch->current_commit->saved_files[k].old_hash = helper1->active_branch->current_commit->saved_files[k].hash_value;
                        helper1->active_branch->current_commit->saved_files[k].hash_value = hash_value;
                        for (int j = 0; j < strlen(name[i]); ++j) {
                            if(name[i][j] != '\0'){
                                c = name[i][j];
                                id = (id * (c % 37)) % 15485863 + 1;
                            }
                        }
                        break;
                    }
                    if(hash_value == helper1->active_branch->current_commit->saved_files[k].hash_value){
                        un_changed +=1 ;
                        break;
                    }
                }
            }
        }// Finish

        //nothing change return null
        if(un_changed == helper1->active_branch->num_tracked){
            return NULL;
        }

        // Create commit
        helper1->active_branch->current_commit->next = calloc(1, sizeof(Commit));
        helper1->active_branch->current_commit->next->previous = helper1->active_branch->current_commit;
        helper1->active_branch->current_commit = helper1->active_branch->current_commit->next;
        helper1->active_branch->current_commit->commit_message = strdup(message);
        helper1->active_branch->current_commit->size_of_files = helper1->active_branch->num_tracked;
        helper1->active_branch->current_commit->commit_id = calloc(7, sizeof(char));
        sprintf(helper1->active_branch->current_commit->commit_id, "%06x", id);
        helper1->active_branch->current_commit->saved_files = calloc(helper1->active_branch->current_commit->size_of_files, sizeof(Tracked_file));
        for (int k = 0; k < helper1->active_branch->current_commit->size_of_files; ++k) {// set commit
            helper1->active_branch->current_commit->saved_files[k].file_path = strdup(helper1->active_branch->file_list[k].file_path);
            helper1->active_branch->current_commit->saved_files[k].hash_value = helper1->active_branch->file_list[k].hash_value;
            helper1->active_branch->current_commit->saved_files[k].status = helper1->active_branch->file_list[k].status;
            helper1->active_branch->current_commit->saved_files[k].old_hash = helper1->active_branch->file_list[k].old_hash;
        }


        ///reset the working branch
        int to_removed = 0;
        for (int l = 0; l < helper1->active_branch->num_tracked; ++l) {
            if(helper1->active_branch->file_list[l].status == -1 ){
                to_removed += 1;
            }
        }
        //  remove all files status equal to -1
        for (int m = 0; m < to_removed; ++m) {
            for (int i = 0; i < helper1->active_branch->num_tracked; ++i) {
                int num_of_files = helper1->active_branch->num_tracked;
                if(helper1->active_branch->file_list[i].status == -1){ // add just now then remove
                    int index_to_remove = i;
                    Tracked_file* copy_array = malloc((num_of_files - 1) * sizeof(Tracked_file));
                    for (int j = 0; j < index_to_remove; ++j) {
                        copy_array[j].status = helper1->active_branch->file_list[j].status;
                        copy_array[j].file_path = strdup(helper1->active_branch->file_list[j].file_path);
                        copy_array[j].hash_value = helper1->active_branch->file_list[j].hash_value;
                        copy_array[j].old_hash = helper1->active_branch->file_list[j].old_hash;
                    }
                    for (int k = index_to_remove; k < num_of_files - 1; k++) {
                        copy_array[k].status = helper1->active_branch->file_list[k+1].status;
                        copy_array[k].file_path = strdup(helper1->active_branch->file_list[k+1].file_path);
                        copy_array[k].hash_value = helper1->active_branch->file_list[k+1].hash_value;
                        copy_array[k].old_hash = helper1->active_branch->file_list[k].old_hash;
                    }

                    for (int j = 0; j < helper1->active_branch->num_tracked; ++j) {
                        free(helper1->active_branch->file_list[j].file_path);
                    }
                    helper1->active_branch->file_list = realloc(helper1->active_branch->file_list, sizeof(Tracked_file)*(num_of_files - 1));
                    // TODO : char * copied_path;
                    for (int l = 0; l < num_of_files - 1; ++l) {
                        helper1->active_branch->file_list[l] = copy_array[l];
                    }
                    free(copy_array);
                    helper1->active_branch->num_tracked -= 1;
                    break;
                }
            }
        }
        // reset all status to 0
        for (int n = 0; n < helper1->active_branch->num_tracked; ++n) {
            helper1->active_branch->file_list[n].status = 0;
        }
    }
    /// Finish

    return helper1->active_branch->current_commit->commit_id;

}

void *get_commit(void *helper, char *commit_id) {
    Helper *helper1 = (Helper *)helper;
    if(commit_id == NULL){
        return NULL;
    }// commit_id is NULL

    for (int i = 0; i < helper1->size_of_branch; i++) {
        Commit* current = helper1->Branch_list[i].current_commit;
        while( current != NULL){
            if(strcmp(current->commit_id, commit_id) == 0){ // have found
                return current;
            }
            current = current->previous;
        }
    }

    return  NULL;// do not exist
}

char **get_prev_commits(void *helper, void *commit, int *n_prev) {
//    Helper *helper1 = (Helper *)helper;
    if(n_prev ==NULL){
        return NULL;
    }
    if(commit == NULL){ // TODO: To learn hpw to know if it is the very first commit
        *n_prev = 0;
        return NULL;
    }
    Commit *commit1 = (Commit *)commit;
    // very first commit
    if(commit1->previous == NULL){
        *n_prev = 0;
        return  NULL;
    }
    char ** parent_commit = calloc(1, sizeof(char *));
    parent_commit[0] = commit1->previous->commit_id;
    *n_prev = 1;

    return parent_commit;
}

void print_commit(void *helper, char *commit_id) {
    Helper *helper1 = (Helper *)helper;
    if(commit_id == NULL){
        printf("Invalid commit id\n");
    }

    int do_not_exists = 1;
    for (int i = 0; i < helper1->size_of_branch; i++) {
        Commit* current = helper1->Branch_list[i].current_commit;
        while( current != NULL){
            if(strcmp(current->commit_id, commit_id) == 0){ // have found
                do_not_exists = 0;
                printf("%s [%s]: %s\n", current->commit_id, helper1->Branch_list[i].name, current->commit_message);
                for (int j = 0; j < current->size_of_files; ++j) {
                    if(current->saved_files[j].status == 1){
                        printf("    + %s\n", current->saved_files[j].file_path);
                    }
                }
                for (int k = 0; k < current->size_of_files; ++k) {
                    if(current->saved_files[k].status == -1){
                        printf("    - %s\n", current->saved_files[k].file_path);
                    }
                }
                for (int l = 0; l < current->size_of_files; ++l) {
                    if(current->saved_files[l].hash_value != current->saved_files[l].old_hash && current->saved_files[l].hash_value != -2){
                        printf("    / %s [%10d ->%10d]\n", current->saved_files[l].file_path, current->saved_files[l].old_hash, current->saved_files[l].hash_value);

                    }
                }
                printf("\n");
                int true_tracked = 0;
                for (int n = 0; n < current->size_of_files; ++n) {
                    if(current->saved_files[n].status != -1){
                        true_tracked += 1;
                    }
                }
                printf("    Tracked files (%d):\n", true_tracked);
                for (int m = 0; m < current->size_of_files; ++m) {
                    if(current->saved_files[m].status != -1){
                        printf("    [%10d] %s\n", current->saved_files[m].hash_value, current->saved_files[m].file_path);
                    }
                }
                break;
            }
            current = current->previous;
        }
    }
    if(do_not_exists == 1){
        printf("Invalid commit id\n");
    }

}

int svc_branch(void *helper, char *branch_name) {
    Helper *helper1 = (Helper *) helper;
    if(branch_name == NULL){

        return -1;
    }// branch_name is  null
    int i = 0;
    while(branch_name[i] != '\0'){
        if('A' > branch_name[i] && branch_name[i] > 'Z' && 'a' > branch_name[i] && branch_name[i] > 'z' && branch_name[i] != '_' && branch_name[i] != '/' && branch_name[i] != '-')
            return -1;
        i++;
    }

    for (int k = 0; k < helper1->size_of_branch; ++k) {
        if(strcmp(helper1->Branch_list[k].name, branch_name) == 0){
            return -2;
        }
    }
    int un_changed = 0;
    for (int l = 0; l < helper1->active_branch->num_tracked; ++l) {
        if(helper1->active_branch->file_list[l].status == 0 && (helper1->active_branch->file_list[l].hash_value == hash_file(helper1, helper1->active_branch->file_list[l].file_path))){
            un_changed += 1;
        }
    }
    if(un_changed != helper1->active_branch->num_tracked){
        return -3;
    }

//    helper1->Branch_list = realloc(helper1->Branch_list ,(helper1->size_of_branch + 1) * sizeof(Branch));
//    helper1->Branch_list[helper1->size_of_branch].name = strdup(branch_name);
//    helper1->Branch_list[helper1->size_of_branch].file_list = NULL;
//    helper1->Branch_list[helper1->size_of_branch].file_list = calloc(helper1->active_branch->num_tracked, sizeof(Tracked_file));
//    helper1->Branch_list[helper1->size_of_branch].num_tracked = 0;
//    helper1->Branch_list[helper1->size_of_branch].num_tracked = helper1->active_branch->num_tracked;
//    for (int m = 0; m < helper1->active_branch->num_tracked; ++m) {
//        helper1->Branch_list[helper1->size_of_branch].file_list[m].status = helper1->active_branch->file_list[m].status;
//        helper1->Branch_list[helper1->size_of_branch].file_list[m].file_path = strdup(helper1->active_branch->file_list[m].file_path);
//        helper1->Branch_list[helper1->size_of_branch].file_list[m].old_hash = helper1->active_branch->file_list[m].old_hash;
//        helper1->Branch_list[helper1->size_of_branch].file_list[m].hash_value = helper1->active_branch->file_list[m].hash_value;
//    }
//    Commit *temp = helper1->active_branch->current_commit;
//    while (temp != NULL){
//        helper1->Branch_list[helper1->size_of_branch].current_commit = calloc(1, sizeof(Commit));
//        helper1->Branch_list[helper1->size_of_branch].current_commit->commit_id = strdup(temp->commit_id);
//        helper1->Branch_list[helper1->size_of_branch].current_commit->commit_message = strdup(temp->commit_message);
//        helper1->Branch_list[helper1->size_of_branch].current_commit->previous = calloc(1, sizeof(Commit));
//        helper1->Branch_list[helper1->size_of_branch].current_commit->next = temp->next;
//        helper1->Branch_list[helper1->size_of_branch].current_commit->size_of_files = temp->size_of_files;
//        helper1->Branch_list[helper1->size_of_branch].current_commit->saved_files = calloc(temp->size_of_files, sizeof(Tracked_file));
//        for (int m = 0; m < helper1->Branch_list[helper1->size_of_branch].current_commit->size_of_files; ++m) {
//            helper1->Branch_list[helper1->size_of_branch].current_commit->saved_files[m].status = temp->saved_files[m].status;
//            helper1->Branch_list[helper1->size_of_branch].current_commit->saved_files[m].file_path = strdup(temp->saved_files[m].file_path);
//            helper1->Branch_list[helper1->size_of_branch].current_commit->saved_files[m].old_hash = temp->saved_files[m].old_hash;
//            helper1->Branch_list[helper1->size_of_branch].current_commit->saved_files[m].hash_value = temp->saved_files[m].hash_value;
//        }
//        temp = temp->previous;
//        helper1->Branch_list[helper1->size_of_branch].current_commit = helper1->Branch_list[helper1->size_of_branch].current_commit->previous;
//    }
//    helper1->Branch_list[helper1->size_of_branch].current_commit = helper1->active_branch->current_commit;
//    helper1->Branch_list += 1;
//    helper1->size_of_branch += 1;
    return 0;
}

int svc_checkout(void *helper, char *branch_name) {
//    Helper *helper1 = (Helper *) helper;
//    if(branch_name == NULL){ // TODO: To learn how to search whether a branch_name exists
//        return -1;
//    }// branch_name is NULL
//    int do_not_exist = 1;
//    for (int i = 0; i < helper1->size_of_branch; ++i) {
//        if(strcmp(branch_name, helper1->Branch_list[i].name) == 0){
//            do_not_exist = 0;
//
//            helper1->active_branch = &(helper1->Branch_list[i]);
//        }
//    }
//    if(do_not_exist == 1){
//        return  -1;
//    }
    return 0;

}

char **list_branches(void *helper, int *n_branches) {
    Helper *helper1 = (Helper *)helper;
    if(n_branches == NULL){
        return NULL;
    }
    char **Branch_name = calloc(helper1->size_of_branch, sizeof(char *));
    for (int i = 0; i < helper1->size_of_branch; ++i) {
        Branch_name[i] = helper1->Branch_list[i].name;
    }
    for (int j = 0; j < helper1->size_of_branch; ++j) {
        printf("%s\n", Branch_name[j]);
    }
    *n_branches = (helper1->size_of_branch);
    return Branch_name;
}

int svc_add(void *helper, char *file_name) {
    Helper *helper1 = (Helper *) helper;
    if(file_name == NULL){
        return -1;
    }
    int num_of_files = helper1->active_branch->num_tracked; // num of files in current branch

    // to check if has been tracked
    for (int i = 0; i < num_of_files; i++) {
        if(strcmp(helper1->active_branch->file_list[i].file_path, file_name) == 0){
            if(helper1->active_branch->file_list[i].status == 1){ // added just now
                return -2;
            }else if(helper1->active_branch->file_list[i].status == 0){ // added before
                return -2;
            } else{
                helper1->active_branch->file_list[i].status = 0; // removed then added , nothing happened
            }
        }
    }
    FILE *fp = fopen(file_name, "r");
    int hash_value = 0;
    if(fp == NULL){
        return -3;// do not exists
    }else{// Add files to working branch and set it status to 1
        if(num_of_files == 0){
            helper1->active_branch->file_list = calloc(num_of_files+1, sizeof(Tracked_file));
            helper1->active_branch->file_list[0].file_path = strdup(file_name);
            helper1->active_branch->file_list[0].status = 1;
            helper1->active_branch->file_list[0].hash_value = hash_file(helper, file_name);
            helper1->active_branch->file_list[0].old_hash = helper1->active_branch->file_list[0].hash_value;
            hash_value = helper1->active_branch->file_list[0].hash_value;
            helper1->active_branch->num_tracked += 1;
        }else{
            helper1->active_branch->file_list = realloc(helper1->active_branch->file_list, sizeof(Tracked_file)*(num_of_files+1));
            helper1->active_branch->file_list[num_of_files].file_path = strdup(file_name);
            helper1->active_branch->file_list[num_of_files].status = 1;
            helper1->active_branch->file_list[num_of_files].hash_value = hash_file(helper, file_name);
            helper1->active_branch->file_list[num_of_files].old_hash = helper1->active_branch->file_list[num_of_files].hash_value;
            hash_value = helper1->active_branch->file_list[num_of_files].hash_value;
            helper1->active_branch->num_tracked += 1;
        }
    }
    return hash_value;
}


int svc_rm(void *helper, char *file_name) {
    Helper *helper1 = (Helper *) helper;
    if(file_name == NULL){
        return -1;
    }
    int do_not_exist = -2;
    int num_of_files = helper1->active_branch->num_tracked; // num of files in current branch
    for (int i = 0; i < num_of_files; i++) {
        if(strcmp(helper1->active_branch->file_list[i].file_path, file_name) == 0){
            // remove immediately if its status is 1
            if(helper1->active_branch->file_list[i].status == 1){
                int index_to_remove = i;
                int hash_value = helper1->active_branch->file_list[i].hash_value;
                // resize the tracked_files array
                Tracked_file* copy_array = malloc((num_of_files - 1) * sizeof(Tracked_file));
                for (int j = 0; j < index_to_remove; ++j) {
                    copy_array[j].status = helper1->active_branch->file_list[j].status;
                    copy_array[j].file_path = strdup(helper1->active_branch->file_list[j].file_path);
                    copy_array[j].hash_value = helper1->active_branch->file_list[j].hash_value;
                    copy_array[j].old_hash = helper1->active_branch->file_list[j].old_hash;
                }
                for (int k = index_to_remove; k < num_of_files - 1; k++) {
                    copy_array[k].status = helper1->active_branch->file_list[k+1].status;
                    copy_array[k].file_path = strdup(helper1->active_branch->file_list[k+1].file_path);
                    copy_array[k].hash_value = helper1->active_branch->file_list[k+1].hash_value;
                    copy_array[k].old_hash = helper1->active_branch->file_list[k+1].old_hash;
                }
                for (int j = 0; j < helper1->active_branch->num_tracked; ++j) {
                    free(helper1->active_branch->file_list[j].file_path);
                }
                helper1->active_branch->file_list = realloc(helper1->active_branch->file_list, sizeof(Tracked_file)*(num_of_files - 1));
                for (int l = 0; l < num_of_files - 1; ++l) {
                    helper1->active_branch->file_list[l] = copy_array[l];
                }
                free(copy_array);
                helper1->active_branch->num_tracked -= 1;
                return hash_value;
            } else if(helper1->active_branch->file_list[i].status == 0){
                // just change working status, remove later 
                helper1->active_branch->file_list[i].status = -1;
                return helper1->active_branch->file_list[i].hash_value;
            }else{
                return -2;
            }

        }
    }

    return do_not_exist;
}

int svc_reset(void *helper, char *commit_id) {
    // TODO: Implement
    return 0;
}

char *svc_merge(void *helper, char *branch_name, struct resolution *resolutions, int n_resolutions) {
    // TODO: Implement
    return NULL;
}

