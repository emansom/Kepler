#include "walk_task.h"

#include "list.h"
#include "deque.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "communication/messages/outgoing_message.h"

#include "shared.h"

void walk_task(void *argument, runnable *self) {
    room *room = argument;

    if (room == NULL || room->users == NULL) {
        free(self);
        self = NULL;
    }

    List *users;
	list_copy_shallow(room->users, &users);

    int players = list_size(users);

    // Stop any segfaults from happening
    if (!(players > 0)) {
        free(self);
        list_destroy(users);
        self = NULL;
    } else {
		int user_updates = 0;
		outgoing_message *status_update = om_create(34);

        // Continue with task
        ListIter iter;
        list_iter_init(&iter, users);

        player *player;
		
        while (list_iter_next(&iter, (void*) &player) != CC_ITER_END) {
            if (player == NULL) {
                continue;
            }

			if (player->room_user->needs_update) {
				player->room_user->needs_update = 0;
				append_user_status(status_update, player);
				user_updates++;
			}
        }
        
		if (user_updates > 0) {
			room_send(room, status_update);
		}
		
		om_cleanup(status_update);

        list_destroy(users);
	    deque_add_last(global.thread_manager.tasks, self);
    }
}