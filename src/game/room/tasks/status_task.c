#include <stdio.h>

#include "hashtable.h"
#include "list.h"

#include "status_task.h"
#include "roller_task.h"

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
        session *room_player;
        list_get_at(room->users, i, (void*)&room_player);
        process_user_status((void*)room_player->room_user);
    }

    /*if (rooms->tick % 3 == 0) {
        do_roller_task(rooms);
    }*/
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

        if (rus->action_switch_countdown > 0) {
            rus->action_switch_countdown--;
        } else if (rus->action_switch_countdown == 0) {
            rus->action_switch_countdown = -1;
            rus->action_countdown = rus->sec_action_switch;

            // Swap back to original key and update status
            rus->key = key;
            room_user->needs_update = true;
        }

        if (rus->action_countdown > 0) {
            rus->action_countdown--;
        } else if (rus->action_countdown == 0) {
            rus->action_countdown = -1;
            rus->action_switch_countdown = rus->sec_switch_lifetime;

            // Swap key to action and update status
            rus->key = rus->action;
            room_user->needs_update = true;
        }

        if (rus->lifetime_countdown > 0) {
            rus->lifetime_countdown--;
        } else if (rus->lifetime_countdown == 0) {
            rus->lifetime_countdown = -1;
            room_user_remove_status(room_user, key);
            room_user->needs_update = true;
        }
    }
}