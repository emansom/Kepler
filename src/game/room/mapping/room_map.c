#include "stdlib.h"
#include "stdio.h"

#include "list.h"

#include "room_tile.h"
#include "room_map.h"

#include "game/items/item.h"
#include "game/room/room.h"
#include "game/room/mapping/room_model.h"

void init_room_map(room *room) {
    if (room->room_map != NULL) {
        room_map_destroy(room);
    } else {
        room->room_map = malloc(sizeof(room_map));

        // Set everything to null...
        for (int x = 0; x < room->room_data->model_data->map_size_x; x++) { 
            for (int y = 0; y < room->room_data->model_data->map_size_y; y++) { 
                room->room_map->map[x][y] = NULL;
            }
        }
    }

    map_regenerate(room);
}

void map_regenerate(room *room) {
    for (int x = 0; x < room->room_data->model_data->map_size_x; x++) { 
        for (int y = 0; y < room->room_data->model_data->map_size_y; y++) { 
            room_tile *tile = room->room_map->map[x][y];

            if (tile != NULL) {
                room_tile_destroy(tile, room);
            }

            tile = room_tile_create(room);
            room->room_map->map[x][y] = tile;

            if (list_size(room->public_items) > 0) {
                ListIter iter;
                list_iter_init(&iter, room->public_items);

                item *room_item;
                while (list_iter_next(&iter, (void*) &room_item) != CC_ITER_END) {
                    if (room_item->x == x && room_item->y == y) {
                        tile->highest_item = room_item;
                        room_tile_add_item(tile, room_item);
                    }
                }
            }
        }
    }
}

void room_map_destroy(room *room) {
    // Set everything to null...
    for (int x = 0; x < room->room_data->model_data->map_size_x; x++) { 
        for (int y = 0; y < room->room_data->model_data->map_size_y; y++) { 
            room_tile *tile = room->room_map->map[x][y];

            if (tile != NULL) {
                room_tile_destroy(tile, room);
            }

            room->room_map->map[x][y] = NULL;
        }
    }
}