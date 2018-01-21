#include "stdlib.h"

#include "game/player/player.h"
#include "game/room/room_user.h"

#include "game/pathfinder/pathfinder.h"
#include "game/pathfinder/coord.h"

#include "hashtable.h"
#include "array.h"

#include "util/stringbuilder.h"
#include "deque.h"
#include "communication/messages/outgoing_message.h"

/*
 *
 */
room_user *room_user_create() {
    room_user *user = malloc(sizeof(room_user));
    user->room_id = 0;
    user->room = NULL;
    user->is_walking = 0;
    user->needs_update = 0;
    user->current = create_coord(0, 0);
    user->goal = create_coord(0, 0);
    user->next = NULL;
    deque_new(&user->walk_list);
    hashtable_new(&user->statuses);
    return user;
}

void walk_to(room_user *room_user, int x, int y) {
    if (room_user->room == NULL) {
        return;
    }

    if (room_user->next != NULL) {
        room_user->current->x = room_user->next->x;
        room_user->current->y = room_user->next->y;
        room_user->needs_update = 1;
    }

    room_user->goal->x = x;
    room_user->goal->y = y;

    Deque *path = create_path(room_user);

    if (path != NULL) {
        if (deque_size(path) > 0) {

            /* start freeing old list */ 
            for (int i = 0; i < (int)deque_size(room_user->walk_list); i++) {
                coord *coord;
                deque_get_at(room_user->walk_list, i, (void*)&coord);
                free(coord);
            }
            /* end freeing old list */ 

            deque_destroy(room_user->walk_list);
            room_user->walk_list = path;
            room_user->is_walking = 1;
        }
    }
}

/**
 * Append user list to the packet
 *
 * @param om the outgoing message
 * @param player the player
 */
void append_user_list(outgoing_message *players, player *player) {
    char instance_id[11];
    sprintf(instance_id, "%i", player->player_data->id);
    instance_id[10] = '\0';

    om_write_str_kv(players, "i", instance_id);
    om_write_str_kv(players, "a", instance_id);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    
    sb_add_string(players->sb, "l");
    sb_add_string(players->sb, ":");
    sb_add_int(players->sb, player->room_user->current->x);
    sb_add_string(players->sb, " ");
    sb_add_int(players->sb, player->room_user->current->y);
    sb_add_string(players->sb, " ");
    sb_add_float(players->sb, player->room_user->current->z);
    sb_add_char(players->sb, 13);

    om_write_str_kv(players, "c", player->player_data->motto);
}

/**
 * Append user statuses to the packet
 *
 * @param om the outgoing message
 * @param player the player
 */
void append_user_status(outgoing_message *om, player *player) {
    sb_add_int(om->sb, player->player_data->id);
    sb_add_string(om->sb, " ");
    sb_add_int(om->sb, player->room_user->current->x);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->current->y);
    sb_add_string(om->sb, ",");
    sb_add_float(om->sb, player->room_user->current->z);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->head_rotation);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->body_rotation);
    sb_add_string(om->sb, "/");

    Array *keys;

    if (hashtable_size(player->room_user->statuses) > 0) {
        hashtable_get_keys(player->room_user->statuses, &keys);

        for (int i = 0; i < array_size(keys); i++) {
            char *key, *value;
            array_get_at(keys, i, (void*)&key);
            hashtable_get(player->room_user->statuses, key, (void*)&value);

            sb_add_string(om->sb, key);
            sb_add_string(om->sb, value);
            sb_add_string(om->sb, "/");
        }    
    }
    sb_add_char(om->sb, 13);
}

void room_user_add_status(room_user *room_user, char *key, char *value) {
    room_user_remove_status(room_user, key);
    hashtable_add(room_user->statuses, key, strdup(value));
}

void room_user_remove_status(room_user *room_user, char *key) {
    if (hashtable_contains_key(room_user->statuses, key)) {
        char *cleanup;
        hashtable_remove(room_user->statuses, key, (void*)&cleanup);
        free(cleanup);
    }
}

/**
 *
 * @param room_user
 */
void room_user_reset(room_user *room_user) {
    room_user->is_walking = 0;
    room_user->needs_update = 0;
    room_user->room_id = 0;
    room_user->room = NULL;

    hashtable_remove_all(room_user->statuses);

    if (room_user->walk_list != NULL) {
        deque_remove_all(room_user->walk_list);
        deque_destroy(room_user->walk_list);
        room_user->walk_list = NULL;
    }

    if (room_user->next != NULL) {
        free(room_user->next);
        room_user->next = NULL;
    }
}

/**
 *
 * @param room_user
 */
void room_user_cleanup(room_user *room_user) {
    if (room_user->current != NULL) {
        free(room_user->current);
        room_user->current = NULL;
    }

    if (room_user->goal != NULL) {
        free(room_user->goal);
        room_user->goal = NULL;
    }
    
    room_user_reset(room_user);
    
    room_user->room = NULL;
    free(room_user);
}