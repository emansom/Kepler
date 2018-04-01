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
room_tile *room_tile_create(room *room) {
    room_tile *tile = malloc(sizeof(room_tile));
    tile->room = room;
    tile->highest_item = NULL;
    list_new(&tile->items);
    list_new(&tile->players);
    return tile;
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