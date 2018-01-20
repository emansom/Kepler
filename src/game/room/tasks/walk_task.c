#include "walk_task.h"

#include "list.h"
#include "deque.h"

#include "game/player/player.h"
#include "game/pathfinder/coord.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_model.h"

#include "communication/messages/outgoing_message.h"

#include "shared.h"

void process_user(player *player);

void walk_task(room *room) {
    List *users;
	list_copy_shallow(room->users, &users);

	int user_updates = 0;
	outgoing_message *status_update = om_create(34);

	ListIter iter;
	list_iter_init(&iter, users);

	player *player;
	while (list_iter_next(&iter, (void*) &player) != CC_ITER_END) {
		if (player == NULL) {
			continue;
		}

		if (player->player_data == NULL) {
			continue;
		}

		if (player->room_user == NULL) {
			continue;
		}

		process_user(player);

		if (player->room_user->needs_update) {
			player->room_user->needs_update = 0;
			append_user_status(status_update, player);
			user_updates++;
		}
	}

	if (user_updates > 0) {
		room_send(room, status_update);
	}
	
	list_destroy(users);
}

void process_user(player *player) {
	room_user *room_user = player->room_user;

	if (room_user->is_walking) {
		if (room_user->next != NULL) {
			room_user->current->x = room_user->next->x;
			room_user->current->y = room_user->next->y;
			room_user->current->z = room_user->room->room_data->model_data->heights[room_user->current->x][room_user->current->y];
		}

		if (deque_size(room_user->walk_list) > 0) {
			coord *next;
			deque_remove_first(room_user->walk_list, (void*)&next);
			room_user->next = next;

		} else {
			room_user->next = NULL;
			room_user->is_walking = 0;
		}

		player->room_user->needs_update = 1;
	}
}