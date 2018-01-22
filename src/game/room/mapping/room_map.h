#ifndef ROOM_MAP_H
#define ROOM_MAP_H

typedef struct list_s List;
typedef struct room_s room;
typedef struct room_tile_s room_tile;

typedef struct room_map_s {
    room_tile *map[200][200];
} room_map;

void init_room_map(room*);
void map_regenerate(room*);
void room_map_destroy(room*);

#endif