#include "stdlib.h"

#include "game/player/player.h"
#include "game/room/room_user.h"

#include "game/pathfinder/pathfinder.h"
#include "game/pathfinder/coord.h"

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
    return user;
}

void walk_to(room_user *room_user, int x, int y) {
    if (room_user->room == NULL) {
        printf("Debug 1\n");
        return;
    }

    if (room_user->next != NULL) {
        room_user->current->x = room_user->next->x;
        room_user->current->y = room_user->next->y;
        room_user->needs_update = 1;
        printf("Debug 2\n");
    }

    room_user->goal->x = x;
    room_user->goal->y = y;

    Deque *path = create_path(room_user);

    if (path != NULL) {
        printf("Debug 3\n");
        if (deque_size(path) > 0) {
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
    sb_add_char(om->sb, 13);
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

    room_user->room = NULL;

    if (room_user->walk_list != NULL) {
        deque_destroy(room_user->walk_list);
        room_user->walk_list = NULL;
    }

    free(room_user);
}