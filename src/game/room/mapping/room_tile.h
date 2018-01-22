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
} room_tile;

room_tile *room_tile_create(room *room);
void room_tile_add_item(room_tile*, item*);
void room_tile_destroy(room_tile*, room*);

#endif