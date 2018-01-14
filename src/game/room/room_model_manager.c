#include "shared.h"
#include "list.h"

#include "database/queries/room_query.h"
#include "room_model.h"

/**
 *
 */
void model_manager_init() {
    list_new(&global.room_model_manager.models);
    room_query_get_models();
}

/**
 *
 * @param model
 */
void model_manager_add(room_model *model) {
    if (model_manager_get(model->type) != NULL) {
        return;
    }

    list_add(global.room_model_manager.models, model);
}

/**
 *
 * @param modeltype
 * @return
 */
room_model *model_manager_get(char *modeltype) {
    ListIter iter;
    list_iter_init(&iter, global.room_model_manager.models);

    room_model *model;

    while (list_iter_next(&iter, (void*) &model) != CC_ITER_END) {
        if (strcmp(model->type, modeltype) == 0) {
            return model;
        }
    }

    return NULL;
}