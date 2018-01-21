#include "stdlib.h"
#include "stdio.h"
#include "string.h"

#include "pathfinder.h"
#include "node.h"
#include "coord.h"

#include "deque.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_model.h"

#include <limits.h>

coord DIAGONAL_MOVE_POINTS[] = {
	{ 0, -1, 0 },
	{ 0, 1, 0 },
	{ 1, 0, 0 },
	{ -1, 0, 0 },
	{ 1, -1, 0 },
	{ -1, 1, 0 },
	{ 1, 1, 0 },
	{ -1, -1, 0 }
};

Deque *create_path(room_user *room_user) {
	if (room_user->room == NULL) {
		return NULL;
	}

	Deque *path;
	deque_new(&path);

	room *room = room_user->room;
	int map_size_x = room->room_data->model_data->map_size_x;
	int map_size_y = room->room_data->model_data->map_size_y;

	pathfinder *pathfinder = NULL;
	pathfinder = make_path_reversed(room_user, map_size_x, map_size_y);

	if (pathfinder->nodes != NULL) {
		while (pathfinder->nodes->node != NULL) {
			node *current_node = pathfinder->nodes;
			int x = pathfinder->nodes->x;
			int y = pathfinder->nodes->y;

			deque_add_first(path, create_coord(x, y));
			pathfinder->nodes = pathfinder->nodes->node;
		}

		for (int x = 0; x < map_size_x; x++) {
			for (int y = 0; y < map_size_y; y++) {
				node *node = pathfinder->map[x][y];
				if (node != NULL) {
					free(node);
					pathfinder->map[x][y] = NULL;
					node = NULL;
				}
			}
		}
	}

	deque_destroy(pathfinder->open_list);
	free(pathfinder);
	
	return path;
}

int is_valid_tile(room_user *room_user, coord from, coord to) {
	room *room = room_user->room;

	// Don't use negative coordinates
	if (from.x < 0 || from.y < 0 || to.x < 0 || to.y < 0) {
		return 0; // 0 for false
	}

	if (from.x >= room->room_data->model_data->map_size_x || from.y >= room->room_data->model_data->map_size_y) {
		return 0;
	}

	if (to.x >= room->room_data->model_data->map_size_x || to.y >= room->room_data->model_data->map_size_y) {
		return 0;
	}

	if (room->room_data->model_data->states[to.x][to.y] == CLOSED) {
		return 0;
	}

	if (room->room_data->model_data->states[from.x][from.y] == CLOSED) {
		return 0;
	}

	return 1; // 1 for true
}

pathfinder *make_path_reversed(room_user *room_user, int map_size_x, int map_size_y) {
	pathfinder *p = malloc(sizeof(pathfinder));
	p->nodes = NULL;
	p->current = NULL;
	deque_new(&p->open_list);

	coord c;
	coord tmp;

	p->current = create_node();
	p->current->x = room_user->current->x;
	p->current->y = room_user->current->y;

    for (int x = 0; x < map_size_x ; x++) { 
		 for (int y = 0; y < map_size_y ; y++) { 
			p->map[x][y] = NULL;
		 }
    }

	int cost = 0;
	int diff = 0;

	p->map[p->current->x][p->current->y] = p->current;
	deque_add_last(p->open_list, p->current);

	while (deque_size(p->open_list) > 0) {
		deque_remove_first(p->open_list, (void*)&p->current);
		p->current->closed = 1;

		for (int i = 0; i < 8; i++) {
			tmp.x = p->current->x + DIAGONAL_MOVE_POINTS[i].x;
			tmp.y = p->current->y + DIAGONAL_MOVE_POINTS[i].y;

			int isFinalMove = (tmp.x == room_user->goal->x && tmp.y == room_user->goal->y);
			c.x = p->current->x;
			c.y = p->current->y;

			if (is_valid_tile(room_user, c, tmp)) {
				if (p->map[tmp.x][tmp.y] == NULL) {
					p->nodes = create_node();
					p->nodes->x = tmp.x;
					p->nodes->y = tmp.y;
					p->map[tmp.x][tmp.y] = p->nodes;

				}
				else {
					p->nodes = p->map[tmp.x][tmp.y];
				}

				if (!p->nodes->closed) {
					diff = 0;

					if (p->current->x != p->nodes->x) {
						diff += 1;
					}

					if (p->current->y != p->nodes->y) {
						diff += 1;
					}

					cost = p->current->cost + diff + distance_squared(p->nodes->x, p->nodes->y, room_user->goal->x, room_user->goal->y);

					if (cost < p->nodes->cost) {
						p->nodes->cost = cost;
						p->nodes->node = p->current;
					}

					if (!p->nodes->open) {
						if (p->nodes->x == room_user->goal->x && p->nodes->y == room_user->goal->y) {
							p->nodes->node = p->current;
							return p;
						}

						p->nodes->open = 1;
						deque_add_last(p->open_list, p->nodes);
					}
				}
			}
		}
	}

	return p;
}