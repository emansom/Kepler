#include "list.h"

#include "room_tile.h"

#include "game/items/item.h"
#include "game/room/room.h"
#include "game/room/mapping/room_model.h"

/**
 * Create the room tile with the list of items and players on each tile.
 *
 * @param room the room struct
 * @return the room tile struct
 */
room_tile *room_tile_create(room *room, int x, int y) {
    room_tile *tile = malloc(sizeof(room_tile));
    tile->room = room;

    list_new(&tile->items);
    list_new(&tile->players);
    return tile;
}

void room_tile_reset(room_tile *tile, room *room, int x, int y) {
    tile->highest_item = NULL;
    tile->tile_height =  room->room_data->model_data->heights[x][y];
    list_remove_all(tile->items);
    list_remove_all(tile->players);
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
    list_destroy(tile->players);
    list_destroy(tile->items);
    free(tile);
}