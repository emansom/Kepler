#ifndef ROOM_TILE_H
#define ROOM_TILE_H

typedef struct list_s List;
typedef struct room_s room;
typedef struct item_s item;

typedef struct room_tile_s {
    item *highest_item;
    room *room;
    List *items;
    List *players;
    double tile_height;
} room_tile;

room_tile *room_tile_create(room *room, int x, int y);
void room_tile_reset(room_tile *tile, room *room, int x, int y);
void room_tile_add_item(room_tile*, item*);
void room_tile_destroy(room_tile*, room*);

#endif