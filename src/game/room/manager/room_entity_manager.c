#include <stdlib.h>
#include <stdio.h>

#include "game/player/player.h"

#include "list.h"

#include "room_entity_manager.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

int create_instance_id(room_user *room_user) {
    int instance_id = 0;

    while (get_room_user_by_instance_id((room*) room_user->room, instance_id) != NULL) {
        instance_id++;
    }

    return instance_id;
}

room_user *get_room_user_by_instance_id(room *room, int instance_id) {
    for (size_t i = 0; i < list_size(room->users); i++) {
        player *room_player;
        list_get_at(room->users, i, (void *) &room_player);

        if (room_player->room_user->instance_id == instance_id) {
            return (room_user*) room_player->room_user;
        }
    }

    return NULL;

}