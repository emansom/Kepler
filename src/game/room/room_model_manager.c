#include "shared.h"
#include "hashtable.h"

#include "database/queries/room_query.h"

void model_manager_init() {
    HashTable *table = room_query_get_models();
    global.room_model_manager.models = table;
}

room_model *model_manager_get(char *modeltype) {
    void *room_model = NULL;

    if (hashtable_contains_key(global.room_model_manager.models, &modeltype)) {
        hashtable_get(global.room_model_manager.models, &modeltype, (void*) &room_model);
    }

    return room_model;
}