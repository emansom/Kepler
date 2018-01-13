#ifndef ROOM_MANAGER_H
#define ROOM_MANAGER_H

typedef struct hashtable_s HashTable;
typedef struct dyad_Stream dyad_Stream;
typedef struct room_s room;

struct room_manager {
    HashTable *rooms;
};

void room_manager_init();

#endif