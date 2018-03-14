#include <stdio.h>

#include "hashtable.h"
#include "list.h"

#include "status_task.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

void process_user_status(room_user *room_user);

void status_task(room *room) {
    for (int i = 0; i < list_size(room->users); i++) {
        player *room_player;
        list_get_at(room->users, i, (void*)&room_player);

        process_user_status(room_player->room_user);
    }
}

void process_user_status(room_user *room_user) {
    Array *keys;

    if (hashtable_size(room_user->statuses) > 0) {
        hashtable_get_keys(room_user->statuses, &keys);

        for (int i = 0; i < array_size(keys); i++) {
            char *key;
            room_user_status *rus;

            array_get_at(keys, i, (void*)&key);
            hashtable_get(room_user->statuses, key, (void*)&rus);

            if (rus->lifetime_expire > 0) {
                rus->lifetime_expire--;
            } else if (rus->lifetime_expire == 0) {
                room_user_remove_status(room_user, "wave");
                room_user->needs_update = 1;
            }
        }
    }
}