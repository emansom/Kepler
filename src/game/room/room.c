#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "room.h"

#include "database/queries/player_query.h"

#include "game/room/room_model_manager.h"
#include "room_model.h"

room *room_create(int room_id) {
    room *instance = malloc(sizeof(room));
    instance->room_id = room_id;
    instance->room_data = NULL;
    return instance;
}

room_data *room_create_data(int id, int owner_id, int category, char *name, char *description, char *model, char *ccts, int wallpaper, int floor, int showname, int superusers, int accesstype, char *password, int visitors_now, int visitors_max) {
    room_data *data = malloc(sizeof(room_data));
    data->id = id;
    data->owner_id = owner_id;
    data->owner_name = query_player_username(owner_id);
    data->category = category;
    data->name = strdup(name);
    data->description = strdup(description);
    data->model = strdup(model);
    data->model_data = model_manager_get(model);
    printf("x: %i and y: %i\n", data->model_data->door_x, data->model_data->door_y);
    data->ccts = strdup(ccts);
    data->wall = wallpaper;
    data->floor = floor;
    data->show_name = showname;
    data->superusers = superusers;
    data->accesstype = accesstype;
    data->password = strdup(password);
    data->visitors_now = visitors_now;
    data->visitors_max = visitors_max;
    return data;
}

void room_cleanup(room *room) {
    if (room->room_data != NULL) {
        free(room->room_data->name);
        free(room->room_data->owner_name);
        free(room->room_data->description);
        free(room->room_data->model);
        free(room->room_data->ccts);
        free(room->room_data->password);
        free(room->room_data);
    }

    free(room);
}