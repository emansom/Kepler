#ifndef ROOM_MODEL_MANAGER_H
#define ROOM_MODEL_MANAGER_H

typedef struct hashtable_s HashTable;
typedef struct room_model_s room_model;

struct room_model_manager {
    HashTable *models;
};

void model_manager_init();
room_model *model_manager_get(char*);

#endif