#include "stdlib.h"
#include "stdio.h"
#include "string.h"

#include "pathfinder.h"
#include "node.h"
#include "coord.h"

#include "deque.h"

#include "game/items/item.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

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

			if (!pathfinder->failed) {
				deque_add_first(path, create_coord(x, y));
			}
			
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

int is_valid_tile(room_user *room_user, coord from, coord to, int is_final_move) {
	room *room_instance = room_user->room;

	if (from.x < 0 || from.y < 0 || to.x < 0 || to.y < 0) {
		return 0;
	}

	if (from.x >= room_instance->room_data->model_data->map_size_x || from.y >= room_instance->room_data->model_data->map_size_y) {
		return 0;
	}

	if (to.x >= room_instance->room_data->model_data->map_size_x || to.y >= room_instance->room_data->model_data->map_size_y) {
		return 0;
	}

	if (room_instance->room_data->model_data->states[to.x][to.y] == CLOSED) {
		return 0;
	}

	if (room_instance->room_data->model_data->states[from.x][from.y] == CLOSED) {
		return 0;
	}

	double old_height = room_instance->room_data->model_data->heights[from.x][from.y];
	double new_height = room_instance->room_data->model_data->heights[to.x][to.y];

	// Can't go down more than 4 in height or can't go up more than 1.5 in height
	if (old_height - 4 >= new_height) {
		return 0;
	}

	if (old_height + 1.5 <= new_height) {
		return 0;
	}

	room_tile *tile = room_instance->room_map->map[to.x][to.y];
	item *item = tile->highest_item;
		
	if (item != NULL) {
		if (item->is_solid == 1) {
			return 0;
		}

		if (is_final_move && item->can_sit) {
			return 1;
		} else {
			return 0;
		}
	}

	return 1; // 1 for true
}

pathfinder *make_path_reversed(room_user *room_user, int map_size_x, int map_size_y) {
	pathfinder *p = malloc(sizeof(pathfinder));
	p->nodes = NULL;
	p->current = NULL;
	p->failed = 0;
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
	deque_add(p->open_list, p->current);

	while (deque_size(p->open_list) > 0) {
		deque_remove_first(p->open_list, (void*)&p->current);
		p->current->closed = 1;

		for (int i = 0; i < 8; i++) {
			tmp.x = p->current->x + DIAGONAL_MOVE_POINTS[i].x;
			tmp.y = p->current->y + DIAGONAL_MOVE_POINTS[i].y;

			int is_final_move = (tmp.x == room_user->goal->x && tmp.y == room_user->goal->y);

			c.x = p->current->x;
			c.y = p->current->y;

			if (is_valid_tile(room_user, c, tmp, is_final_move)) {
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
						deque_add(p->open_list, p->nodes);
					}
				}
			}
		}
	}

	p->failed = 1;
	return p;
}