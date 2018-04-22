#include <stdlib.h>
#include <stdio.h>

#include "game/player/player.h"

#include "list.h"

#include "room_entity_manager.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

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