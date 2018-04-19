#ifndef ROOM_MAP_H
#define ROOM_MAP_H

typedef struct list_s List;
typedef struct room_s room;
typedef struct room_tile_s room_tile;

typedef struct room_map_s {
    room_tile *map[200][200];
} room_map;

void room_map_init(room *);
void room_map_regenerate(room *);
void room_map_add_item(room *room, item *item);
void room_map_remove_item(room *room, item *item);
void room_map_item_adjustment(room *room, item *item, bool rotation);
void room_map_destroy(room*);


#endif