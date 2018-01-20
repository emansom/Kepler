#include "stdlib.h"

#include "game/player/player.h"
#include "game/room/room_user.h"

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
    deque_new(&user->walk_list);
    return user;
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
    sb_add_int(players->sb, player->room_user->x);
    sb_add_string(players->sb, " ");
    sb_add_int(players->sb, player->room_user->y);
    sb_add_string(players->sb, " ");
    sb_add_float(players->sb, player->room_user->z);
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
    sb_add_int(om->sb, player->room_user->x);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->y);
    sb_add_string(om->sb, ",");
    sb_add_float(om->sb, player->room_user->z);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->head_rotation);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->body_rotation);
    sb_add_string(om->sb, "/");
    sb_add_char(om->sb, 13);
}

/**
 *
 * @param room_user
 */
void room_user_cleanup(room_user *room_user) {
    deque_destroy(room_user->walk_list);
    free(room_user);
}