#include <stdio.h>

#include "hashtable.h"
#include "list.h"

#include "status_task.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

void process_user_status(room_user *room_user);

/**
 * Status task cycle that is called every 1000ms.
 *
 * @param room the room struct to process
 */
void status_task(room *room) {
    for (size_t i = 0; i < list_size(room->users); i++) {
        player *room_player;
        list_get_at(room->users, i, (void*)&room_player);
        process_user_status((void*)room_player->room_user);
    }
}

/**
 * Process the user in the status task cycle
 *
 * @param player the player struct to process
 */
void process_user_status(room_user *room_user) {
    if (hashtable_size(room_user->statuses) == 0) {
        return;
    }

    HashTableIter iter;
    hashtable_iter_init(&iter, room_user->statuses);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        char *key = entry->key;
        room_user_status *rus = entry->value;

        if (rus->lifetime_expire > 0) {
            rus->lifetime_expire--;
        } else if (rus->lifetime_expire == 0) {
            room_user_remove_status(room_user, key);
            room_user->needs_update = 1;
        }
    }
}