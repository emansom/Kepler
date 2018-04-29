#include "list.h"

#include "room_tile.h"

#include "game/items/item.h"

#include "game/room/room.h"
#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"

/**
 * Create the room tile with the list of items and players on each tile.
 *
 * @param room the room struct
 * @return the room tile struct
 */
room_tile *room_tile_create(room *room, int x, int y) {
    room_tile *tile = malloc(sizeof(room_tile));
    tile->room = room;
    tile->x = x;
    tile->y = y;
    tile->entity = NULL;
    tile->highest_item = NULL;
    list_new(&tile->items);
    return tile;
}

/**
 * Reset a tile to its defaults.
 *
 * @param tile the tile to reset
 * @param room the room the tile was in
 */
void room_tile_reset(room_tile *tile, room *room) {
    tile->highest_item = NULL;
    tile->entity = NULL;
    tile->tile_height =  room->room_data->model_data->heights[tile->x][tile->y];
    list_remove_all(tile->items);
}

/**
 * Get if the tile is walkable.
 *
 * @param room the room to check inside
 * @param room_user the room user to check for (may be NULL)
 * @param x the x coordinate to check
 * @param y the y coordinate to check
 * @return true, if successful
 */
bool room_tile_is_walkable(room *room, room_user *room_user, int x, int y) {
    if (x < 0 || y < 0) {
        return false;
    }

    if (x >= room->room_data->model_data->map_size_x || y >= room->room_data->model_data->map_size_y) {
        return false;
    }

    if (room->room_data->model_data->states[x][y] == CLOSED) {
        return false;
    }

    room_tile *tile = room->room_map->map[x][y];

    if (tile == NULL) {
        return false;
    }

    if (list_size(room->room_data->model_data->public_items) == 0) {
        if (tile->entity != NULL && room_user != NULL) {
            if (tile->entity != room_user) {
                return false;
            }
        }
    }

    if (tile->highest_item != NULL) {
        if (!item_is_walkable(tile->highest_item)) {
            return false;
        }
    }

    return true;
}

/**
 * Add an item to the list of items in the tile if it doesn't already exist.
 *
 * @param tile the room tile struct
 * @param item the item struct to add
 */
void room_tile_add_item(room_tile *tile, item *item) {
    if (!list_contains(tile->items, item)) {
        list_add(tile->items, item);
    }
}

/**
 * Destroy the room tile struct along with the players and items list.
 *
 * @param tile the room tile struct
 * @param room the room struct
 */
void room_tile_destroy(room_tile *tile, room *room) {
    list_destroy(tile->items);
    free(tile);
}