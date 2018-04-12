#include <stdlib.h>
#include <stdio.h>

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

/*
 *
 */
room_user *room_user_create(player *player) {
    room_user *user = malloc(sizeof(room_user));
    user->player = player;
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
 *
 * @param room_user
 * @param x
 * @param y
 */
void walk_to(room_user *room_user, int x, int y) {
    if (room_user->room == NULL) {
        return;
    }

    room_tile *tile = room_user->room->room_map->map[x][y];

    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        if (strcmp(item->class_name, "queue_tile2") == 0 && room_user->player->player_data->tickets == 0) {
            outgoing_message *om = om_create(73); // "AI"
            player_send((player *) room_user->player, om);
            om_cleanup(om);
            return;
        }
    }

    if (room_user->next != NULL) {
        room_user->current->x = room_user->next->x;
        room_user->current->y = room_user->next->y;
        room_user->needs_update = true;
    }

    room_user->goal->x = x;
    room_user->goal->y = y;



    //printf("User requested path %i, %i from path %i, %i in room %i.\n", x, y, room_user->current->x, room_user->current->y, room_user->room_id);

    /*room_tile *tile = room_user->room->room_map->map[room_user->goal->x][room_user->goal->y];

    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        strcmp(to_item->class_name, "queue_tile2")

        printf("Item: %s\n", item->class_name);
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
    }
}

/**
 * Forcibly stops the user from walking, will clear/update statuses and auto manage memory.
 *
 * @param room_user the room user
 */
void stop_walking(room_user *room_user, bool is_silent) {
    room_user_remove_status(room_user, "mv");
    room_user_clear_walk_list(room_user);
    room_user->is_walking = false;

    bool needs_update = false;

    if (!is_silent) {
        item *item = NULL;

        room_tile *tile = room_user->room->room_map->map[room_user->current->x][room_user->current->y];

        if (tile != NULL) {
            if (tile->highest_item != NULL) {
                item = tile->highest_item;
            }
        }

        if (item == NULL || !item->can_sit) {
            if (room_user_has_status(room_user, "sit") || room_user_has_status(room_user, "lay")) {
                room_user_remove_status(room_user, "sit");
                room_user_remove_status(room_user, "lay");
                needs_update = true;
            }
        }

        if (item != NULL) {
            if (item->can_sit) {
                room_user_add_status(room_user, "sit", " 1.0", -1, "", 0, 0);
                coord_set_rotation(room_user->current, item->coords->rotation ,item->coords->rotation);
                needs_update = true;
            }

            pool_item_walk_on((player *) room_user->player, item);
        }
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
    if (room_user->room != NULL) {
        stop_walking(room_user, false);
    }

    room_user->is_walking = 0;
    room_user->needs_update = 0;
    room_user->room_id = 0;
    room_user->room = NULL;
    room_user->walking_lock = false;
    room_user->lido_vote = -1;

    room_user_clear_walk_list(room_user);

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
 * Append user list to the packet
 *
 * @param om the outgoing message
 * @param player the player
 */
void append_user_list(outgoing_message *players, player *player) {
    char user_id[11], instance_id[11];
    sprintf(user_id, "%i", player->player_data->id);
    sprintf(instance_id, "%i", player->room_user->instance_id);

    om_write_str_kv(players, "i", instance_id);
    om_write_str_kv(players, "a", user_id);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    om_write_str_kv(players, "s", player->player_data->sex);
    sb_add_string(players->sb, "l");
    sb_add_string(players->sb, ":");
    sb_add_int(players->sb, player->room_user->current->x);
    sb_add_string(players->sb, " ");
    sb_add_int(players->sb, player->room_user->current->y);
    sb_add_string(players->sb, " ");
    sb_add_float(players->sb, player->room_user->current->z);
    sb_add_char(players->sb, 13);

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
void append_user_status(outgoing_message *om, player *player) {
    sb_add_int(om->sb, player->room_user->instance_id);
    sb_add_string(om->sb, " ");
    sb_add_int(om->sb, player->room_user->current->x);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->current->y);
    sb_add_string(om->sb, ",");
    sb_add_float(om->sb, player->room_user->current->z);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->current->head_rotation);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->current->body_rotation);
    sb_add_string(om->sb, "/");

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