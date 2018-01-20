#include "stdlib.h"
#include "stdio.h"
#include "string.h"

#include "pathfinder.h"
#include "node.h"
#include "coord.h"

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

int get_address(int x, int y, int map_size_y) {
	return x * map_size_y + y;
}

void start_pathfinder_test() {
	coord from;
	from.x = 1;
	from.y = 1;

	coord to;
	to.x = 5;
	to.y = 9;

	int map_size_x = 20;
	int map_size_y = 20;

	pathfinder *pathfinder = make_path_reversed(from, to, map_size_x, map_size_y);

	if (pathfinder->nodes != NULL) {
		while (pathfinder->nodes->node != NULL) {
			node *current_node = pathfinder->nodes;
			int x = pathfinder->nodes->x;
			int y = pathfinder->nodes->y;

			printf("x: %d\n", x);
			printf("y: %d\n", y);

			pathfinder->nodes = pathfinder->nodes->node;
		}


		for (int y = 0; y < map_size_y; y++) {
			for (int x = 0; x < map_size_x; x++) {
				node *node = pathfinder->map[get_address(x, y, map_size_y)];

				if (node != NULL) {
					free(node);
					pathfinder->map[get_address(x, y, map_size_y)] = NULL;
				}
			}
		}

		free(pathfinder->map);
		free(pathfinder->open_list);
		free(pathfinder);

	}
}

int is_valid_tile(coord from, coord to) {
	// Don't use negative coordinates
	if (from.x < 0 || from.y < 0 || to.x < 0 || to.y < 0) {
		return 0; // 0 for false
	}

	// TODO: Add your checking here
	// The "to" variable is the tile around the "from" variable.

	return 1; // 1 for true
}

pathfinder *make_path_reversed(coord from, coord to, int map_size_x, int map_size_y) {
	int map_size = map_size_x * map_size_y;

	pathfinder *p = malloc(sizeof(pathfinder));
	p->map = malloc(sizeof(node) * map_size);
	p->open_list = malloc(sizeof(node) * map_size);

	node **adjust_list = p->open_list;
	memset(p->map, 0, sizeof(node) * map_size);
	memset(p->open_list, 0, sizeof(node) * map_size);

	p->current = create_node();
	p->current->x = from.x;
	p->current->y = from.y;

	coord tmp;

	int cost = 0;
	int diff = 0;

	p->map[get_address(p->current->x, p->current->y, map_size_y)] = p->current;
	p->open_list[0] = p->current;

	int list_size = 1;

	while (adjust_list[0] != 0) {
		p->current = adjust_list[0];
		p->current->closed = 1;

		adjust_list++;
		list_size--;

		for (int i = 0; i < 8; i++) {
			tmp.x = p->current->x + DIAGONAL_MOVE_POINTS[i].x;
			tmp.y = p->current->y + DIAGONAL_MOVE_POINTS[i].y;

			int isFinalMove = (tmp.x == to.x && tmp.y == to.y);

			coord c;
			c.x = p->current->x;
			c.y = p->current->y;

			if (is_valid_tile(c, tmp)) {
				int array_index = get_address(tmp.x, tmp.y, map_size_y);

				if (p->map[array_index] == 0) {
					p->nodes = create_node();
					p->nodes->x = tmp.x;
					p->nodes->y = tmp.y;
					p->map[array_index] = p->nodes;
				}
				else {
					p->nodes = p->map[array_index];
				}

				if (!p->nodes->closed) {
					diff = 0;

					if (p->current->x != p->nodes->x) {
						diff += 1;
					}

					if (p->current->y != p->nodes->y) {
						diff += 1;
					}

					cost = p->current->cost + diff + distance_squared(p->nodes->x, p->nodes->y, to.x, to.y);

					if (cost < p->nodes->cost) {
						p->nodes->cost = cost;
						p->nodes->node = p->current;
					}

					if (!p->nodes->open) {
						if (p->nodes->x == to.x && p->nodes->y == to.y) {
							p->nodes->node = p->current;
							return p;
						}

						p->nodes->open = 1;
						adjust_list[list_size++] = p->nodes;
					}
				}
			}
		}
	}

	return p;
}