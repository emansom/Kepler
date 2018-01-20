#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "list.h"
#include "util/stringbuilder.h"
#include "deque.h"

#include "room.h"
#include "room_model.h"
#include "room_user.h"

#include "tasks/walk_task.h"

#include "game/player/player.h"
#include "game/items/item.h"
#include "game/navigator/navigator_category_manager.h"
#include "game/pathfinder/coord.h"

#include "database/queries/player_query.h"
#include "communication/messages/outgoing_message.h"


/**
 * Create a room instance.
 * 
 * @param room_id the room id
 * @return the room instance
 */
room *room_create(int room_id) {
    room *instance = malloc(sizeof(room));
    instance->room_id = room_id;
    instance->room_data = NULL;
    list_new(&instance->users);
    list_new(&instance->public_items);
    return instance;
}

/**
 * Create a room data instance.
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
room_data *room_create_data(room *room, int id, int owner_id, int category, char *name, char *description, char *model, char *ccts, int wallpaper, int floor, int showname, int superusers, int accesstype, char *password, int visitors_now, int visitors_max) {
    room_data *data = malloc(sizeof(room_data));
    data->id = id;
    data->owner_id = owner_id;
    data->owner_name = query_player_username(owner_id);
    data->category = category;
    data->name = strdup(name);
    data->description = strdup(description);
    data->model_data = model_manager_get(model);
    data->model = data->model_data->model_name;

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

    List *public_items = data->model_data->public_items;

    if (list_size(public_items) > 0) {
        ListIter iter;
        list_iter_init(&iter, public_items);

        item *room_item;

        while (list_iter_next(&iter, (void*) &room_item) != CC_ITER_END) {
            list_add(room->public_items, room_item);
        }
    }

    return data;
}

/**
 * Room entry handler.
 *
 * @param om the outgoing message
 * @param player the player
 */
void room_enter(room *room, player *player) {
    if (list_contains(room->users, player)) {
        return;
    }

    if (player->room_user->room != NULL) {
        room_leave(player->room_user->room, player);
    }

    if (room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", room->room_data->id);
        return;
    }

    player->room_user->room = room;
    player->room_user->room_id = room->room_id;

    player->room_user->current->x = room->room_data->model_data->door_x;
    player->room_user->current->y = room->room_data->model_data->door_y;
    player->room_user->current->z = room->room_data->model_data->door_z;
    player->room_user->head_rotation = room->room_data->model_data->door_dir;
    player->room_user->body_rotation = room->room_data->model_data->door_dir;

    if (list_size(room->users) == 0) {
        runnable *r = malloc(sizeof(runnable));
        r->request = walk_task;
        r->argument = room;
        deque_add_last(global.thread_manager.tasks, r);
    }

    list_add(room->users, player);
    room->room_data->visitors_now++;
}

/**
 * Leave room handler, will make room and id for the room user reset back to NULL and 0.
 * And remove the character from the room.
 */
void room_leave(room *room, player *player) {
    if (!list_contains(room->users, player)) {
        return;
    }

    player->room_user->room = NULL;
    player->room_user->room_id = 0;

    list_remove(room->users, player, NULL);
    room->room_data->visitors_now--;

    outgoing_message *om = om_create(29); // "@]"
    sb_add_int(om->sb, player->player_data->id);
    room_send(room, om);
    om_cleanup(om);
}

/**
 * Send packets to start the room entance.
 *
 * @param om the outgoing message
 * @param player the player
 */
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
}

/**
 * Send an outgoing message to all the room users.
 * 
 * @param room the room
 * @param message the outgoing message to send
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
 * Cleanup a room instance.
 * 
 * @param room the room instance.
 */
void room_cleanup(room *room) {
    if (room->room_data != NULL) {
        free(room->room_data->name);
        free(room->room_data->owner_name);
        free(room->room_data->description);
        free(room->room_data->ccts);
        free(room->room_data->password);
        free(room->room_data);
    }

    list_destroy(room->users);
    list_destroy(room->public_items);

    room->users = NULL;
    room->public_items = NULL;

    free(room);
}