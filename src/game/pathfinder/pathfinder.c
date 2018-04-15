#include "stdlib.h"
#include "stdio.h"
#include "string.h"

#include "pathfinder.h"
#include "node.h"
#include "coord.h"

#include "deque.h"

#include "game/player/player.h"
#include "game/items/item.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

#include <limits.h>
#include <game/items/definition/item_definition.h>

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

/**
 *
 * @param room_user
 * @return
 */
Deque *create_path(room_user *room_user) {
    if (room_user->room == NULL) {
        return NULL;
    }

    Deque *path;
    deque_new(&path);

    room *user_room = (void *)room_user->room;
    int map_size_x = user_room->room_data->model_data->map_size_x;
    int map_size_y = user_room->room_data->model_data->map_size_y;

    pathfinder *pathfinder = NULL;
    pathfinder = make_path_reversed(room_user, map_size_x, map_size_y);

    if (pathfinder->nodes != NULL) {
        while (pathfinder->nodes->node != NULL) {
            int x = pathfinder->nodes->x;
            int y = pathfinder->nodes->y;

            if (!pathfinder->failed) {
                deque_add_first(path, create_coord(x, y));
            }
            
            pathfinder->nodes = (void *)pathfinder->nodes->node;
        }

        for (int x = 0; x < map_size_x; x++) {
            for (int y = 0; y < map_size_y; y++) {
                node *node = pathfinder->map[x][y];
                if (node != NULL) {
                    free(node);
                    pathfinder->map[x][y] = NULL;
                }
            }
        }
    }

    deque_destroy(pathfinder->open_list);
    free(pathfinder);
    
    return path;
}

/**
 *
 * @param room_user
 * @param from
 * @param to
 * @param is_final_move
 * @return
 */
int is_valid_tile(room_user *room_user, coord from, coord to, int is_final_move) {
    room *room_instance = (void *)room_user->room;

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

    if (old_height - 4 >= new_height) {
        return 0;
    }

    if (old_height + 1.5 <= new_height) {
        return 0;
    }

    room_tile *from_tile = room_instance->room_map->map[from.x][from.y];
    room_tile *to_tile = room_instance->room_map->map[to.x][to.y];

    item *to_item = to_tile->highest_item;
    item *from_item = from_tile->highest_item;

    if (from_item != NULL) {
        if (strcmp(from_item->class_name, "poolEnter") == 0 || strcmp(from_item->class_name, "poolExit") == 0) {
            return strlen(room_user->player->player_data->pool_figure) > 0;
        }

        if (to_item != NULL) {
            if (strcmp(room_user->room->room_data->model_data->model_name, "pool_b") == 0
                && strcmp(from_item->class_name, "queue_tile2") == 0
                && strcmp(to_item->class_name, "queue_tile2") == 0) {

                return true;
            }
        }
    }

    if (to_item != NULL) {
        if (strcmp(to_item->class_name, "poolLift") == 0 || strcmp(to_item->class_name, "poolBooth") == 0) {
            if (to_item->current_program_state != NULL && strcmp(to_item->current_program_state, "close") == 0) {
                return 0;
            } else {
                return strcmp(to_item->class_name, "poolLift") == 0 ? strlen(room_user->player->player_data->pool_figure) > 0 : true;
            }
        }

        if (strcmp(room_user->room->room_data->model_data->model_name, "pool_b") == 0
            && strcmp(to_item->class_name, "queue_tile2") == 0) {

            if (to_item->coords->x == 21 && to_item->coords->y == 9) {
                return room_user->player->player_data->tickets > 0 && strlen(room_user->player->player_data->pool_figure) > 0;
            } else {
                return false;
            }
        }

        if (from_item == NULL) {
            if (is_final_move && (to_item->definition->behaviour->canSitOnTop || to_item->definition->behaviour->canStandOnTop)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    return 1; // 1 for true
}

/**
 *
 * @param room_user
 * @param map_size_x
 * @param map_size_y
 * @return
 */
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

                    cost = p->current->cost + diff + distance_squared(tmp, *room_user->goal);

                    if (cost < p->nodes->cost) {
                        p->nodes->cost = cost;
                        p->nodes->node = (void *)p->current;
                    }

                    if (!p->nodes->open) {
                        if (p->nodes->x == room_user->goal->x && p->nodes->y == room_user->goal->y) {
                            p->nodes->node = (void *)p->current;
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