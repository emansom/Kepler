#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "list.h"
#include "util/stringbuilder.h"

#include "room.h"
#include "room_model.h"
#include "room_user.h"

#include "game/player/player.h"
#include "database/queries/player_query.h"
#include "communication/messages/outgoing_message.h"

/**
 *
 * @param room_id
 * @return
 */
room *room_create(int room_id) {
    room *instance = malloc(sizeof(room));
    instance->room_id = room_id;
    instance->room_data = NULL;
    list_new(&instance->users);
    return instance;
}

/**
 *
 * @param id
 * @param owner_id
 * @param category
 * @param name
 * @param description
 * @param model
 * @param ccts
 * @param wallpaper
 * @param floor
 * @param showname
 * @param superusers
 * @param accesstype
 * @param password
 * @param visitors_now
 * @param visitors_max
 * @return
 */
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
    
    if (ccts == NULL) {
        data->ccts = strdup("");
    } else {
        data->ccts = strdup(ccts);
    }

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

/**
 * Room entry handler
 *
 * @param om the outgoing message
 * @param player the player
 */
void room_enter(room *room, player *player) {
    if (list_contains(room->users, player)) {
        return;
    }

    player->room_user->room = room;
    player->room_user->room_id = room->room_id;

    if (player->room_user->room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", player->room_user->room->room_data->id);
        return;
    }

    player->room_user->x = room->room_data->model_data->door_x;
    player->room_user->y = room->room_data->model_data->door_y;
    player->room_user->z = room->room_data->model_data->door_z;
    player->room_user->head_rotation = 2;
    player->room_user->body_rotation = 2;

    list_add(room->users, player);
}

void room_load(room *room, player *player) {
    outgoing_message *om = om_create(166); // "Bf"
    om_write_str(om, "/client/");
    player_send(player, om);
    om_cleanup(om);

    om = om_create(69); // "AE"
    om_write_str(om, player->room_user->room->room_data->model);
    om_write_str(om, " ");
    om_write_str_int(om, player->room_user->room_id);
    player_send(player, om);
    om_cleanup(om);

    om = om_create(345); // "EY"
    om_write_int(om, 0); // votes
    player_send(player, om);
    om_cleanup(om);
}

/**
 *
 * @param room
 * @param message
 */
void room_send(room *room, outgoing_message *message) {
    ListIter iter;
    list_iter_init(&iter, room->users);

    player *player;
    while (list_iter_next(&iter, (void*) &player) != CC_ITER_END) {
        player_send(player, message);
    }
}

/**
 *
 * @param room
 */
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