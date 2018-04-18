#include <game/items/definition/item_definition.h>
#include "stdlib.h"
#include "stdio.h"

#include "list.h"

#include "room_tile.h"
#include "room_map.h"

#include "game/pathfinder/coord.h"

#include "game/items/item.h"

#include "game/room/room.h"
#include "game/room/mapping/room_model.h"
#include "game/room/pool/pool_handler.h"

void room_map_add_public_items(room *room);
void room_map_add_private_items(room *room);

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

    room_map_add_private_items(room);
    room_map_add_public_items(room);
}

/**
 * Add private rooms to the room collision map.
 *
 * @param room the room instance
 */
void room_map_add_private_items(room *room) {
    for (size_t i = 0; i < list_size(room->items); i++) {
        item *public_item;
        list_get_at(room->items, i, (void *) &public_item);

        room_tile *tile = room->room_map->map[public_item->coords->x][public_item->coords->y];

        if (tile != NULL) {
            tile->highest_item = public_item;
            room_tile_add_item(tile, public_item);
        }
    }
}

/**
 * Add public rooms to the room collision map.
 *
 * @param room the room instance
 */
void room_map_add_public_items(room *room) {
    for (size_t i = 0; i < list_size(room->items); i++) {
        item *public_item;
        list_get_at(room->items, i, (void *) &public_item);

        if (!public_item->definition->behaviour->isPublicSpaceObject) {
            continue;
        }

        room_tile *tile = room->room_map->map[public_item->coords->x][public_item->coords->y];

        if (tile != NULL) {
            tile->highest_item = public_item;
            room_tile_add_item(tile, public_item);

            pool_setup_redirections(room, public_item);
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