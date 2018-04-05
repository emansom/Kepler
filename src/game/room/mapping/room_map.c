#include "stdlib.h"
#include "stdio.h"

#include "list.h"

#include "room_tile.h"
#include "room_map.h"

#include "game/items/item.h"
#include "game/room/room.h"
#include "game/room/mapping/room_model.h"

void room_map_add_public_items(room *room);
void room_map_reset(room *room);

/**
 * Initalises the room map for the furniture collision.
 *
 * @param room the room instance
 */
void room_map_init(room *room) {
    if (room->room_map == NULL) {
        room->room_map = malloc(sizeof(room_map));

        for (int x = 0; x < room->room_data->model_data->map_size_x; x++) {
            for (int y = 0; y < room->room_data->model_data->map_size_y; y++) {
                room->room_map->map[x][y] = room_tile_create(room);
            }
        }
    }

    room_map_regenerate(room);
}
/**
 * Regenerates the room map.
 *
 * @param room the room instance
 */
void room_map_regenerate(room *room) {
    for (int x = 0; x < room->room_data->model_data->map_size_x; x++) { 
        for (int y = 0; y < room->room_data->model_data->map_size_y; y++) { 
            room_tile *tile = room->room_map->map[x][y];
            list_remove_all(tile->items);
            list_remove_all(tile->players);
        }
    }

    room_map_add_public_items(room);
}

/**
 * Add public rooms to the room collision map.
 *
 * @param room the room instance
 */
void room_map_add_public_items(room *room) {
    for (size_t i = 0; i < list_size(room->public_items); i++) {
        item *public_item;
        list_get_at(room->public_items, i, (void *) &public_item);

        room_tile *tile = room->room_map->map[public_item->x][public_item->y];

        if (tile != NULL) {
            tile->highest_item = public_item;
            room_tile_add_item(tile, public_item);
        }
    }
}

/**
 * Destroy the room map instance.
 *
 * @param room the room instance
 */
void room_map_destroy(room *room) {
    if (room->room_map != NULL) {
        for (int x = 0; x < room->room_data->model_data->map_size_x; x++) {
            for (int y = 0; y < room->room_data->model_data->map_size_y; y++) {
                room_tile_destroy(room->room_map->map[x][y], room);
                room->room_map->map[x][y] = NULL;
            }
        }

        free(room->room_map);
        room->room_map = NULL;
    }

}