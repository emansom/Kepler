#include "walk_task.h"

#include "list.h"
#include "deque.h"

#include "game/player/player.h"

#include "game/pathfinder/coord.h"
#include "game/pathfinder/rotation.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/items/item.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

#include "communication/messages/outgoing_message.h"

#include "shared.h"

void process_user(player *player);

void walk_task(room *room) {
    List *users;// = room->users;
    list_copy_shallow(room->users, &users);

	int user_updates = 0;
	outgoing_message *status_update = om_create(34); // "@b"

	for (int i = 0; i < list_size(users); i++) {
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
	} else {
		om_cleanup(status_update);
	}

	list_destroy(users);
}

void process_user(player *player) {
	room_user *room_user = player->room_user;

	if (room_user->is_walking) {
		if (room_user->next != NULL) {
			room_user->current->x = room_user->next->x;
			room_user->current->y = room_user->next->y;
			room_user->current->z = room_user->next->z;
			free(room_user->next);
		}

		if (deque_size(room_user->walk_list) > 0) {
			coord *next;
			deque_remove_first(room_user->walk_list, (void*)&next);
			next->z = room_user->room->room_data->model_data->heights[next->x][next->y];

			char value[30];
			sprintf(value, " %i,%i,%.2f", next->x, next->y, next->z);

			int rotation = calculate(room_user->current->x, room_user->current->y, next->x, next->y);
			room_user->body_rotation = rotation;
			room_user->head_rotation = rotation;

			room_user_add_status(room_user, "mv", value);
			room_user->next = next;


		} else {
			room_user->next = NULL;
			room_user->is_walking = 0;

			room_tile *tile = room_user->room->room_map->map[room_user->current->x][room_user->current->y];
			stop_walking(room_user, tile->highest_item);
		}

		player->room_user->needs_update = 1;
	}
}