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
    List *users = room->users;
	int user_count = list_size(users);

	int user_updates = 0;
	outgoing_message *status_update = om_create(34);

	for (int i = 0; i < user_count; i++) {
		player *room_player;
		list_get_at(users, i, (void*)&room_player);

		if (room_player == NULL) {
			continue;
		}

		if (room_player->room_user == NULL) {
			continue;
		}

		process_user(room_player);

		if (room_player->room_user->needs_update) {
			room_player->room_user->needs_update = 0;
			user_updates++;
			append_user_status(status_update, room_player);
		}

	}

	if (user_updates > 0) {
		room_send(room, status_update);
	}
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

			char *key = "mv";
			char value[30];
			sprintf(value, " %i,%i,%f", next->x, next->y, next->z);
			room_user_add_status(room_user, key, value);


		} else {
			room_user->next = NULL;
			room_user->is_walking = 0;
			room_user_remove_status(room_user, "mv");
		}

		player->room_user->needs_update = 1;
	}
}