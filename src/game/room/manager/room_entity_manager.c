#include <stdlib.h>
#include <stdio.h>

#include "list.h"
#include "hashtable.h"

#include "game/pathfinder/coord.h"
#include "game/player/player.h"

#include "room_entity_manager.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/mapping/room_model.h"

#include "util/stringbuilder.h"

#include "communication/messages/outgoing_message.h"

/**
 * Get a unused instance id for the room they're in.
 *
 * @param room_user the room user struct to assign to
 * @return the new instance id
 */
int create_instance_id(room_user *room_user) {
    int instance_id = 0;

    while (get_room_user_by_instance_id((room*) room_user->room, instance_id) != NULL) {
        instance_id++;
    }

    return instance_id;
}

/**
 * Find a room user by instance id.
 *
 * @param room the room to search in
 * @param instance_id the instance id to search for
 * @return the room user struct
 */
room_user *get_room_user_by_instance_id(room *room, int instance_id) {
    for (size_t i = 0; i < list_size(room->users); i++) {
        session *room_player;
        list_get_at(room->users, i, (void *) &room_player);

        if (room_player->room_user->instance_id == instance_id) {
            return (room_user*) room_player->room_user;
        }
    }

    return NULL;
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
    sb_add_int_delimeter(players->sb, player->room_user->position->x, ' ');
    sb_add_int_delimeter(players->sb, player->room_user->position->y, ' ');
    sb_add_float_delimeter(players->sb, player->room_user->position->z, (char)13);

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
    sb_add_int_delimeter(om->sb, player->room_user->position->x, ',');
    sb_add_int_delimeter(om->sb, player->room_user->position->y, ',');
    sb_add_float_delimeter(om->sb, player->room_user->position->z, ',');
    sb_add_int_delimeter(om->sb, player->room_user->position->head_rotation, ',');
    sb_add_int_delimeter(om->sb, player->room_user->position->body_rotation, '/');

    if (hashtable_size(player->room_user->statuses) > 0) {
        HashTableIter iter;

        TableEntry *entry;
        hashtable_iter_init(&iter, player->room_user->statuses);

        while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
            room_user_status *rus = entry->value;

            sb_add_string(om->sb, rus->key);
            sb_add_string(om->sb, rus->value);
            sb_add_string(om->sb, "/");
        }
    }

    sb_add_char(om->sb, 13);
}