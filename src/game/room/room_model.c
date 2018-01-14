#include "stdlib.h"

#include "room_model.h"
#include "shared.h"

/**
 *
 * @param modeltype
 * @param door_x
 * @param door_y
 * @param door_z
 * @param heightmap
 * @return
 */
room_model *room_model_create(char *modeltype, int door_x, int door_y, double door_z, char *heightmap) {
    room_model *model = malloc(sizeof(room_model));
    model->type = strdup(modeltype);
    model->door_x = door_x;
    model->door_y = door_y;
    model->door_z = door_z;
    model->heightmap = replace(heightmap, '|', "\r");
    return model;
}