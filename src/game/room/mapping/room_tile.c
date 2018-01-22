#include "list.h"

#include "room_tile.h"

#include "game/items/item.h"
#include "game/room/room.h"
#include "game/room/mapping/room_model.h"

room_tile *room_tile_create(room *room) {
    room_tile *tile = malloc(sizeof(room_tile));
    tile->room = room;
    tile->highest_item = NULL;
    list_new(&tile->items);
    list_new(&tile->players);
    return tile;
}

void room_tile_add_item(room_tile *tile, item *item) {
    if (!list_contains(tile->items, item)) {
        list_add(tile->items, item);
    }
}

void room_tile_destroy(room_tile *tile, room *room) {
    list_destroy(tile->players);
    list_destroy(tile->items);
    free(tile);
}