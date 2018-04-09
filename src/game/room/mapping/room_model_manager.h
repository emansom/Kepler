#ifndef ROOM_MODEL_MANAGER_H
#define ROOM_MODEL_MANAGER_H

typedef struct list_s List;
typedef struct room_model_s room_model;

struct room_model_manager {
    List *models;
};

void model_manager_init();
void model_manager_add(room_model*);
room_model *model_manager_get(char*);
void model_manager_dispose();

#endif