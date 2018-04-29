#include <stdlib.h>
#include <stdio.h>
#include <game/items/definition/item_definition.h>

#include "array.h"
#include "hashtable.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_tile.h"
#include "game/room/mapping/room_map.h"
#include "game/room/pool/pool_handler.h"

#include "game/items/item.h"

#include "game/pathfinder/pathfinder.h"
#include "game/pathfinder/coord.h"

#include "util/stringbuilder.h"
#include "deque.h"
#include "communication/messages/outgoing_message.h"

/**
 * Create room user by the given entity.
 *
 * @param player the entity for the room user
 * @return the room user struct to return
 */
room_user *room_user_create(session *player) {
    room_user *user = malloc(sizeof(room_user));
    user->player = player;
    user->authenticate_id = 0;
    user->instance_id = 0;
    user->room_id = 0;
    user->room = NULL;
    user->is_walking = 0;
    user->needs_update = 0;
    user->lido_vote = -1;
    user->current = create_coord(0, 0);
    user->goal = create_coord(0, 0);
    user->next = NULL;
    user->walk_list = NULL;
    user->walking_lock = false;
    hashtable_new(&user->statuses);
    return user;
}

/**
 * Send walk request to room.
 *
 * @param room_user the room user that wants to walk
 * @param x the x coord
 * @param y the y corord
 */
void walk_to(room_user *room_user, int x, int y) {
    if (room_user->room == NULL) {
        return;
    }

    //printf("User requested path %i, %i from path %i, %i in room %i.\n", x, y, room_user->current->x, room_user->current->y, room_user->room_id);

    if (!room_tile_is_walkable((room *) room_user->room, room_user, x, y)) {
        return;
    }

    room_tile *tile = room_user->room->room_map->map[x][y];

    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        if (strcmp(item->definition->sprite, "queue_tile2") == 0 && room_user->player->player_data->tickets == 0) {
            outgoing_message *om = om_create(73); // "AI"
            player_send((session *) room_user->player, om);
            om_cleanup(om);
            return;
        }
    }

    if (room_user->next != NULL) {
        room_user->current->x = room_user->next->x;
        room_user->current->y = room_user->next->y;
        room_user->needs_update = true;

        free(room_user->next);
        room_user->next = NULL;
    }

    room_user->goal->x = x;
    room_user->goal->y = y;

    //printf("User requested path %i, %i from path %i, %i in room %i.\n", x, y, room_user->current->x, room_user->current->y, room_user->room_id);

    /*room_tile *tiles = room_user->room->room_map->map[room_user->goal->x][room_user->goal->y];

    if (tile != NULL && tiles->highest_item != NULL) {
        item *items = tiles->highest_item;
        printf("Item: %s and height %f\n", items->definition->sprite, item_total_height(items));
    }*/

    Deque *path = create_path(room_user);
    
    if (path != NULL && deque_size(path) > 0) {
        room_user_clear_walk_list(room_user);
        room_user->walk_list = path;
        room_user->is_walking = true;
    }
}

/**
 * Clear the walk list, called by the server automatically.
 *
 * @param room_user the room user to clear
 */
void room_user_clear_walk_list(room_user *room_user) {
    if (room_user->walk_list != NULL) {
        for (size_t i = 0; i < (int)deque_size(room_user->walk_list); i++) {
            coord *coord;
            deque_get_at(room_user->walk_list, i, (void*)&coord);
            free(coord);
        }

        deque_destroy(room_user->walk_list);
        room_user->walk_list = NULL;
        room_user->next = NULL;
    }
}

/**
 * Forcibly stops the user from walking, will clear/update statuses and auto manage memory.
 *
 * @param room_user the room user
 */
void stop_walking(room_user *room_user, bool is_silent) {
    if (room_user->next != NULL) {
        free(room_user->next);
        room_user->next = NULL;
    }

    room_user_remove_status(room_user, "mv");
    room_user_clear_walk_list(room_user);
    room_user->is_walking = false;

    if (!is_silent) {
        room_user_invoke_item(room_user);
    }
}

/**
 * Animates the users mouth when speaking and detects any gestures.
 *
 * @param room_user the room user to animate for
 * @param text the text to read for any gestures and to find animation times
 */
void room_user_move_mouth(room_user *room_user, char *text) {
    int talk_duration = 1;

    if (strlen(text) > 1) {
        if (strlen(text) >= 10) {
            talk_duration = 5;
        } else {
            talk_duration = (int) (strlen(text) / 2);
        }
    }

    bool found_gesture = false;
    char gesture[5];

    if (strstr(text, ":)") != NULL
        || strstr(text, ":-)") != NULL
        || strstr(text, ":p") != NULL
        || strstr(text, ":d") != NULL
        || strstr(text, ":D") != NULL
        || strstr(text, ";)") != NULL
        || strstr(text, ";-)") != NULL) {
        strcpy(gesture, " sml");
        found_gesture = true;
    }

    if (!found_gesture &&
        strstr(text, ":s") != NULL
        || strstr(text, ":(") != NULL
        || strstr(text, ":-(") != NULL
        || strstr(text, ":'(") != NULL) {
        strcpy(gesture, " sad");
        found_gesture = true;
    }

    if (!found_gesture &&
        strstr(text, ":o") != NULL
        || strstr(text, ":O") != NULL) {
        strcpy(gesture, " srp");
        found_gesture = true;
    }


    if (!found_gesture &&
        strstr(text, ":@") != NULL
        || strstr(text, ">:(") != NULL) {
        strcpy(gesture, " agr");
        found_gesture = true;
    }

    if (found_gesture) {
        room_user_add_status(room_user, "gest", gesture, 5, "", -1, -1);
    }

    room_user_add_status(room_user, "talk", "", talk_duration, "", -1, -1);
    room_user->needs_update = true;
}

/**
 * Triggers the current item that the player on top of.
 *
 * @param room_user the room user to trigger for
 */
void room_user_invoke_item(room_user *room_user) {
    bool needs_update = false;

    double height = room_user->room->room_map->map[room_user->current->x][room_user->current->y]->tile_height;

    if (height != room_user->current->z) {
        room_user->current->z = height;
        needs_update = true;
    }

    item *item = NULL;
    room_tile *tile = room_user->room->room_map->map[room_user->current->x][room_user->current->y];

    if (tile != NULL) {
        if (tile->highest_item != NULL) {
            item = tile->highest_item;
        }
    }

    if (item == NULL || (!item->definition->behaviour->can_sit_on_top && !item->definition->behaviour->can_lay_on_top)) {
        if (room_user_has_status(room_user, "sit") || room_user_has_status(room_user, "lay")) {
            room_user_remove_status(room_user, "sit");
            room_user_remove_status(room_user, "lay");
            needs_update = true;
        }
    }

    if (item != NULL) {
        if (item->definition->behaviour->can_sit_on_top) {
            char sit_height[11];
            sprintf(sit_height, " %1.f", item->definition->top_height);

            room_user_add_status(room_user, "sit", sit_height, -1, "", 0, 0);
            coord_set_rotation(room_user->current, item->position->rotation ,item->position->rotation);
            needs_update = true;
        }

        pool_item_walk_on((session*) room_user->player, item);
    }



    room_user->needs_update = needs_update;
}

/**
 * Adds a status to the room user, will handle switching actions automatically in the status task.
 * Will automatically remove and free the previous status.
 *
 * @param room_user the room user
 * @param key the first part to the status
 * @param value the second part to the status
 * @param sec_lifetime seconds until the status expires, -1 for permanent
 * @param action the action to switch to
 * @param sec_action_switch the amount of seconds needed until the action gets switched
 * @param sec_action_length the amount of seconds needed for the action to stay until the action switches back
 */
void room_user_add_status(room_user *room_user, char *key, char *value, int sec_lifetime, char *action, int sec_action_switch, int sec_action_length) {
    room_user_remove_status(room_user, key);

    room_user_status *status = malloc(sizeof(room_user_status));
    status->value = strdup(value);
    status->sec_lifetime = sec_lifetime;
    status->lifetime_expire = sec_lifetime;
    status->action = strdup(action);
    status->sec_action_switch = sec_action_switch;
    status->sec_action_length = sec_action_length;
    status->action_expire = sec_action_length;
    hashtable_add(room_user->statuses, key, status);
}

/**
 * Removes a status of the room user by status key. Will
 * automatically remove and free the previous status.
 *
 * @param room_user the room user
 * @param key the key to remove
 */
void room_user_remove_status(room_user *room_user, char *key) {
    if (hashtable_contains_key(room_user->statuses, key)) {
        room_user_status *cleanup;
        hashtable_remove(room_user->statuses, key, (void*)&cleanup);
        free(cleanup->value);
        free(cleanup->action);
        free(cleanup);
    }
}

/**
 * Returns if the user currently has a status by its key.
 *
 * @param room_user the room user
 * @param key the key to remove
 * @return true, if successful
 */
int room_user_has_status(room_user *room_user, char *key) {
    return hashtable_contains_key(room_user->statuses, key);
}

/**
 *  Called when a player either leaves a room, or disconnects.
 * 
 * @param room_user
 */
void room_user_reset(room_user *room_user) {
    stop_walking(room_user, true);
    room_user_remove_status(room_user, "swim");
    room_user_remove_status(room_user, "sit");
    room_user_remove_status(room_user, "lay");
    room_user_remove_status(room_user, "flatctrl");

    room_user->is_walking = 0;
    room_user->needs_update = 0;
    room_user->room_id = 0;
    room_user->room = NULL;
    room_user->walking_lock = false;
    room_user->lido_vote = -1;
    room_user->authenticate_id = 0;

    if (room_user->next != NULL) {
        free(room_user->next);
        room_user->next = NULL;
    }
}

/**
 * Called when a player disconnects.
 * 
 * @param room_user
 */
void room_user_cleanup(room_user *room_user) {
    room_user_reset(room_user);
    
    if (room_user->current != NULL) {
        free(room_user->current);
        room_user->current = NULL;
    }

    if (room_user->goal != NULL) {
        free(room_user->goal);
        room_user->goal = NULL;
    }

    if (room_user->statuses != NULL) {
        hashtable_destroy(room_user->statuses);
        room_user->statuses = NULL;
    }
    
    room_user->room = NULL;
    free(room_user);
}

/**
 * Append user list to the packet.
 *
 * @param om the outgoing message
 * @param player the player
 */
void append_user_list(outgoing_message *players, session *player) {
    char user_id[11], instance_id[11];
    sprintf(user_id, "%i", player->player_data->id);
    sprintf(instance_id, "%i", player->room_user->instance_id);

    om_write_str_kv(players, "i", instance_id);
    om_write_str_kv(players, "a", user_id);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    om_write_str_kv(players, "s", player->player_data->sex);
    sb_add_string(players->sb, "l:");
    sb_add_int_delimeter(players->sb, player->room_user->current->x, ' ');
    sb_add_int_delimeter(players->sb, player->room_user->current->y, ' ');
    sb_add_float_delimeter(players->sb, player->room_user->current->z, (char)13);

    if (strlen(player->player_data->motto) > 0) {
        om_write_str_kv(players, "c", player->player_data->motto);
    }

    if (strcmp(player->room_user->room->room_data->model_data->model_name, "pool_a") == 0
        || strcmp(player->room_user->room->room_data->model_data->model_name, "pool_b") == 0
        || strcmp(player->room_user->room->room_data->model_data->model_name, "md_a") == 0) {

        if (strlen(player->player_data->pool_figure) > 0) {
            om_write_str_kv(players, "p", player->player_data->pool_figure);
        }
    }
}

/**
 * Append user statuses to the packet
 *
 * @param om the outgoing message
 * @param player the player
 */
void append_user_status(outgoing_message *om, session *player) {
    sb_add_int_delimeter(om->sb, player->room_user->instance_id, ' ');
    sb_add_int_delimeter(om->sb, player->room_user->current->x, ',');
    sb_add_int_delimeter(om->sb, player->room_user->current->y, ',');
    sb_add_float_delimeter(om->sb, player->room_user->current->z, ',');
    sb_add_int_delimeter(om->sb, player->room_user->current->head_rotation, ',');
    sb_add_int_delimeter(om->sb, player->room_user->current->body_rotation, '/');

    if (hashtable_size(player->room_user->statuses) > 0) {
        HashTableIter iter;

        TableEntry *entry;
        hashtable_iter_init(&iter, player->room_user->statuses);

        while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
            char *key = entry->key;
            room_user_status *rus = entry->value;

            sb_add_string(om->sb, key);
            sb_add_string(om->sb, rus->value);
            sb_add_string(om->sb, "/");
        }
    }

    sb_add_char(om->sb, 13);
}