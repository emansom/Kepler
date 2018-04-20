#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "shared.h"

#include "list.h"
#include "util/stringbuilder.h"
#include "util/threading.h"

#include "deque.h"
#include "thpool.h"

#include "room.h"
#include "room_user.h"

#include "mapping/room_model.h"
#include "mapping/room_model_manager.h"
#include "mapping/room_map.h"
#include "mapping/room_tile.h"

#include "manager/room_item_manager.h"
#include "manager/room_entity_manager.h"

#include "tasks/walk_task.h"
#include "tasks/status_task.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "game/items/item.h"
#include "game/navigator/navigator_category_manager.h"
#include "game/pathfinder/coord.h"

#include "database/queries/player_query.h"
#include "communication/messages/outgoing_message.h"

void room_load_data(room *room);

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
    list_new(&instance->items);
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
    data->owner_name = player_query_username(owner_id);
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

        while (list_iter_next(&iter, (void*)&room_item) != CC_ITER_END) {
            room_item->room_id = id;
            list_add(room->items, room_item);
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
    if (player->room_user->room != NULL) {
        room_leave(player->room_user->room, player);
    }

    if (list_size(room->users) == 0) {
        room_load_data(room);
    }

    if (room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", room->room_data->id);
        return;
    }
    
    room_user *room_entity = (room_user *) player->room_user;

   room_entity->room = room;
   room_entity->room_id = room->room_id;
   room_entity->instance_id = create_instance_id((room_user*)room_entity);

   room_entity->current->x = room->room_data->model_data->door_x;
   room_entity->current->y = room->room_data->model_data->door_y;
   room_entity->current->z = room->room_data->model_data->door_z;

   coord_set_rotation(room_entity->current,
      room->room_data->model_data->door_dir,
      room->room_data->model_data->door_dir);

    list_add(room->users, player);
    room->room_data->visitors_now = list_size(room->users);

    if (room->walking_job == NULL) {
        room->walking_job = create_runnable();
        room->walking_job->request = walk_task;
        room->walking_job->room = (void*) room;
        room->walking_job->room_id = room->room_id;
        room->walking_job->millis = 500;
        thpool_add_work(global.thread_manager.pool, (void*)do_room_task, room->walking_job);
    }

    if (room->status_job == NULL) {
        room->status_job = create_runnable();
        room->status_job->request = status_task;
        room->status_job->room = (void*) room;
        room->status_job->room_id = room->room_id;
        room->status_job->millis = 1000;
        thpool_add_work(global.thread_manager.pool, (void*)do_room_task, room->status_job);
    }

    /*outgoing_message *om = om_create(73); // "AI"
    player_send(player, om);
    om_cleanup(om);*/
}

/**
 * Used to load data if they're the first to enter the room.
 *
 * @param room the room to load the data for
 */
void room_load_data(room *room) {
    room_item_manager_load(room);
    room_map_init(room);
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

    room_tile *current_tile = room->room_map->map[room_player->room_user->current->x][room_player->room_user->current->y];
    current_tile->entity = NULL;

    room_user_reset((room_user*) room_player->room_user);

    outgoing_message *om = om_create(29); // "@]"
    sb_add_int(om->sb, room_player->room_user->instance_id);
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
    sb_add_string(om->sb, room->room_data->model);
    sb_add_string(om->sb, " ");
    sb_add_int(om->sb, room->room_id);
    player_send(player, om);
    om_cleanup(om);
}

/**
 * Get if the user id is owner of the room.
 *
 * @param room the room to check owner for
 * @param user_id the id to check against
 * @return true, if successful
 */
bool room_is_owner(room *room, int user_id) {
    return room->room_data->owner_id == user_id;
}

/**
 * Get if the player has rights.
 *
 * @param room the room to check rights for
 * @param user_id the user id to check if they have rights
 * @return true, if successful
 */
bool room_has_rights(room *room, int user_id) {
    if (room->room_data->superusers) {
        return true;
    }

    return true;
}

/**
 * Refresh the room rights for the user.
 *
 * @param room the room to refresh inside for
 * @param player the player to refresh the rights for
 */
void room_refresh_rights(room *room, player *player) {
    char rights_value[15];
    strcpy(rights_value, "");

    outgoing_message *om;

    if (room_has_rights(room, player->player_data->id)) {
        om = om_create(42); // "@j"
        player_send(player, om);
        om_cleanup(om);
    }

    if (room_is_owner(room, player->player_data->id)) {
        om = om_create(47); // "@o"
        player_send(player, om);
        om_cleanup(om);

        strcpy(rights_value, " useradmin");
    }

    room_user *room_entity = (room_user*) player->room_user;
    room_user_remove_status(room_entity, "flatctrl");

    if (room_has_rights(room, player->player_data->id) || room_is_owner(room, player->player_data->id)) {
        room_user_add_status(room_entity, "flatctrl", rights_value, -1, "", -1, -1);
        room_entity->needs_update = true;
    }
}

/**
 * Send an outgoing message to all the room users.
 * 
 * @param room the room
 * @param message the outgoing message to send
 */
void room_send(room *room, outgoing_message *message) {
    om_finalise(message);

    for (size_t i = 0; i < list_size(room->users); i++) {
        player *room_player;
        list_get_at(room->users, i, (void*)&room_player);
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

    room_map_destroy(room);

    if (list_size(room->room_data->model_data->public_items) > 0) { // model is a public room model
        return; // Prevent public rooms
    }

    room_item_manager_dispose(room);

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
        room->room_data = NULL;
    }

    list_destroy(room->users);
    list_destroy(room->items);

    room->users = NULL;
    room->walking_job = NULL;
    room->status_job = NULL;
    
    free(room);
    room = NULL;
}