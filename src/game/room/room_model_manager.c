#include "shared.h"
#include "hashtable.h"

#include "database/queries/room_query.h"

void model_manager_init() {
    hashtable_new(&global.room_model_manager.models);
    room_query_get_models();
}

void model_manager_add(char *modeltype, room_model *model) {
    if (hashtable_contains_key(global.room_model_manager.models, &modeltype)) {
        return;
    }

    hashtable_add(global.room_model_manager.models, modeltype, model);
}

room_model *model_manager_get(char *modeltype) {
    room_model *room_model = NULL;

    if (hashtable_contains_key(global.room_model_manager.models, &modeltype)) {
        printf("hello! %i\n", hashtable_size(global.room_model_manager.models));
        hashtable_get(global.room_model_manager.models, &modeltype, (void*) &room_model);
    } else {
        printf("could not find! %i\n", hashtable_size(global.room_model_manager.models));
    }

    return room_model;
}