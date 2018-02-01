#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "list.h"
#include "util/stringbuilder.h"
#include "deque.h"
#include "thpool.h"

#include "room.h"
#include "room_user.h"

#include "mapping/room_model.h"
#include "mapping/room_map.h"

#include "tasks/walk_task.h"
#include "tasks/status_task.h"

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
    instance->room_map = NULL;
    instance->walking_job = NULL;
    instance->status_job = NULL;
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
    if (list_size(room->users) == 0) {
        init_room_map(room);
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

    list_add(room->users, player);
    room->room_data->visitors_now = list_size(room->users);

    if (room->walking_job == NULL) {
        room->walking_job = create_runnable();
        room->walking_job->request = walk_task;
        room->walking_job->room = room;
        room->walking_job->room_id = room->room_id;
        room->walking_job->millis = 500;
        thpool_add_work(global.thread_manager.pool, (void*)do_room_task, room->walking_job);
    }

    if (room->status_job == NULL) {
        room->status_job = create_runnable();
        room->status_job->request = status_task;
        room->status_job->room = room;
        room->status_job->room_id = room->room_id;
        room->status_job->millis = 1000;
        thpool_add_work(global.thread_manager.pool, (void*)do_room_task, room->status_job);
    }
}

/**
 * Leave room handler, will make room and id for the room user reset back to NULL and 0.
 * And remove the character from the room.
 */
void room_leave(room *room, player *room_player) {
    if (!list_contains(room->users, room_player)) {
        return;
    }

    list_remove(room->users, room_player, NULL);
    room->room_data->visitors_now = list_size(room->users);

    room_user_reset(room_player->room_user); 

    outgoing_message *om = om_create(29); // "@]"
    sb_add_int(om->sb, room_player->player_data->id);
    room_send(room, om);

    room_player->room_user->room = NULL;
    room_dispose(room);
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
    sb_add_string(om->sb, player->room_user->room->room_data->model);
    sb_add_string(om->sb, " ");
    sb_add_int(om->sb, player->room_user->room_id);
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
    om_finalise(message);

	for (int i = 0; i < list_size(room->users); i++) {
		player *room_player;
		list_get_at(room->users, i, (void*)&room_player);

		if (room_player == NULL) {
			continue;
		}

		if (room_player->room_user == NULL) {
			continue;
		}

        player_send(room_player, message);
	}

	om_cleanup(message);
}

/**
 * Cleanup a room instance.
 * 
 * @param room the room instance.
 */
void room_dispose(room *room) {
    if (list_size(room->users) > 0) {
        return;
    }

    if (list_size(room->public_items) > 0) {
        return; // Prevent public rooms
    }

    if (player_manager_find_by_id(room->room_data->owner_id) != NULL) {
        return;
    }

    room_manager_remove(room->room_id);

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
    room->walking_job = NULL;
    room->status_job = NULL;
    
    free(room);
    room = NULL;
}