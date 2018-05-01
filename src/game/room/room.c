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

#include "room_task.h"
#include "tasks/status_task.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "game/items/item.h"
#include "game/navigator/navigator_category_manager.h"
#include "game/pathfinder/coord.h"

#include "database/queries/player_query.h"
#include "database/queries/rooms/room_query.h"
#include "database/queries/rooms/room_vote_query.h"

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
    instance->room_schedule_job = NULL;
    list_new(&instance->users);
    list_new(&instance->items);
    instance->tick = 0;
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
room_data *room_create_data(room *room, int id, int owner_id, int category, char *name, char *description, char *model, char *ccts, int wallpaper, int floor, int showname, bool superusers, int accesstype, char *password, int visitors_now, int visitors_max) {
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

    data->wallpaper = wallpaper;
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

void room_append_data(room *instance, outgoing_message *navigator, int player_id) {
    if (list_size(instance->room_data->model_data->public_items) > 0) {
        om_write_int(navigator, instance->room_data->id); // rooms id
        om_write_int(navigator, 1);
        om_write_str(navigator, instance->room_data->name);
        om_write_int(navigator, instance->room_data->visitors_now); // current visitors
        om_write_int(navigator, instance->room_data->visitors_max); // max vistors
        om_write_int(navigator, instance->room_data->category); // category id
        om_write_str(navigator, instance->room_data->description); // description
        om_write_int(navigator, instance->room_data->id); // rooms id
        om_write_int(navigator, 0);
        om_write_str(navigator, instance->room_data->ccts);
        om_write_int(navigator, 0);
        om_write_int(navigator, 1);
    } else {
        om_write_int(navigator, instance->room_data->id); // rooms id
        om_write_str(navigator, instance->room_data->name);

        if (player_id == instance->room_data->owner_id || instance->room_data->show_name == 1) {
            om_write_str(navigator, instance->room_data->owner_name); // rooms owner
        } else {
            om_write_str(navigator, "-"); // rooms owner
        }

        if (instance->room_data->accesstype == 2) {
            om_write_str(navigator, "password");
        }

        if (instance->room_data->accesstype == 1) {
            om_write_str(navigator, "closed");
        }

        if (instance->room_data->accesstype == 0) {
            om_write_str(navigator, "open");
        }

        om_write_int(navigator, instance->room_data->visitors_now); // current visitors
        om_write_int(navigator, instance->room_data->visitors_max); // max vistors
        om_write_str(navigator, instance->room_data->description); // description
    }
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
 * Room entry handler.
 *
 * @param om the outgoing message
 * @param player the player
 */
void room_enter(room *room, session *player) {
    if (list_size(room->users) == 0) {
        room_load_data(room);
    }

    if (room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", room->room_data->id);
        return;
    }

    room_user *room_entity = player->room_user;

    room_entity->room = room;
    room_entity->room_id = room->room_id;
    room_entity->instance_id = create_instance_id(room_entity);

    room_entity->position->x = room->room_data->model_data->door_x;
    room_entity->position->y = room->room_data->model_data->door_y;
    room_entity->position->z = room->room_data->model_data->door_z;

    coord_set_rotation(room_entity->position,
                       room->room_data->model_data->door_dir,
                       room->room_data->model_data->door_dir);

    list_add(room->users, player);
    room->room_data->visitors_now = (int) list_size(room->users);

    if (room->room_schedule_job == NULL) {
        room->room_schedule_job = create_runnable();
        room->room_schedule_job->request = room_task;
        room->room_schedule_job->room = room;
        room->room_schedule_job->room_id = room->room_id;
        room->room_schedule_job->millis = 500;
        thpool_add_work(global.thread_manager.pool, (void *) do_room_task, room->room_schedule_job);
    }

    /*outgoing_message *om = om_create(73); // "AI"
    player_send(session, om);
    om_cleanup(om);*/
}

/**
 * Leave room handler, will make room and id for the room user reset back to NULL and 0.
 * And remove the character from the room.
 */
void room_leave(room *room, session *room_player, bool hotel_view) {
    if (!list_contains(room->users, room_player)) {
        return;
    }

    list_remove(room->users, room_player, NULL);
    room->room_data->visitors_now = (int) list_size(room->users);

    // Remove current user from tile
    room_tile *current_tile = room->room_map->map[room_player->room_user->position->x][room_player->room_user->position->y];
    current_tile->entity = NULL;

    outgoing_message *om;

    // Reset item program state for pool items
    item *item = current_tile->highest_item;
    if (item != NULL) {
        if (item->current_program != NULL &&
            (strcmp(item->current_program, "curtains1") == 0
             || strcmp(item->current_program, "curtains2") == 0
             || strcmp(item->current_program, "door") == 0)) {

            item_assign_program(item, "open");
        }
    }

    // Reset rooms user
    room_user_reset(room_player->room_user);

    // Make figure vanish from the rooms
    om = om_create(29); // "@]"
    sb_add_int(om->sb, room_player->room_user->instance_id);
    room_send(room, om);

    room_player->room_user->room = NULL;
    room_dispose(room, false);

    // Go to hotel view, if told so.
    if (hotel_view) {
        om = om_create(18); // "@R"
        player_send(room_player, om);
        om_cleanup(om);
    }
}

/**
 * Kick all users from the current room
 * @param room
 */
void room_kickall(room *room) {
    for (size_t i = 0; i < list_size(room->users); i++) {
        session *user;
        list_get_at(room->users, i, (void *) &user);
        room_leave(room, user, true);
    }
}

/**
 * Send packets to start the room entance.
 *
 * @param om the outgoing message
 * @param player the player
 */
void room_load(room *room, session *player) {
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

    if (room->room_data->wallpaper > 0) {
        om = om_create(46); // "@n"
        sb_add_string(om->sb, "wallpaper/");
        sb_add_int(om->sb, room->room_data->wallpaper);
        player_send(player, om);
        om_cleanup(om);
    }

    if (room->room_data->floor > 0) {
        om = om_create(46); // "@n"
        sb_add_string(om->sb, "floor/");
        sb_add_int(om->sb, room->room_data->floor);
        player_send(player, om);
        om_cleanup(om);
    }

    // Show new session current state of an item program for pools
    for (size_t i = 0; i < list_size(room->items); i++) {
        item *item;
        list_get_at(room->items, i, (void *) &item);

        if (!item->definition->behaviour->is_public_space_object) {
            continue;
        }

        if (item->current_program != NULL &&
            (strcmp(item->current_program, "curtains1") == 0
             || strcmp(item->current_program, "curtains2") == 0
             || strcmp(item->current_program, "door") == 0)) {

            om = om_create(71); // "AG"
            sb_add_string(om->sb, item->current_program);

            if (item->current_program_state != NULL && strlen(item->current_program_state) > 0) {
                sb_add_string(om->sb, " ");
                om_write_str(om, item->current_program_state);
            }

            player_send(player, om);
            om_cleanup(om);
        }
    }

    // TODO: move votes to rooms object and load on initialization to reduce query load

    // Check if already voted, return if voted
    int voted = room_query_check_voted(room->room_data->id, player->player_data->id);
    int vote_count = -1;

    // If user already has voted, we sent total vote count
    // else we sent -1, making the vote selector pop up
    if (voted != -1) {
        vote_count = room_query_count_votes(room->room_data->id);
    }

    om = om_create(345); // "EY"
    om_write_int(om, vote_count);
    player_send(player, om);
    om_cleanup(om);

    player->room_user->authenticate_id = -1;
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
    if (room->room_data->owner_id == user_id) { // Of course rooms owners have rights, duh!
        return true;
    }

    if (room->room_data->superusers) {
        return true;
    }

    return false;
}

/**
 * Refresh the room rights for the user.
 *
 * @param room the room to refresh inside for
 * @param player the player to refresh the rights for
 */
void room_refresh_rights(room *room, session *player) {
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
        session *room_player;
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
void room_dispose(room *room, bool override) {
    if (list_size(room->users) > 0) {
        return;
    }

    room->tick = 0;
    room_map_destroy(room);

    if (list_size(room->room_data->model_data->public_items) > 0) { // model is a public rooms model
        return; // Prevent public rooms
    }

    room_item_manager_dispose(room);

    if (!override) {
        if (player_manager_find_by_id(room->room_data->owner_id) != NULL) {
            return;
        }
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

    for (size_t i = 0; i < list_size(room->items); i++) {
        item *item;
        list_get_at(room->items, i, (void *) &item);
        item_dispose(item);
    }

    list_destroy(room->users);
    list_destroy(room->items);

    room->users = NULL;
    room->room_schedule_job = NULL;

    free(room);
    room = NULL;
}