#ifndef ROOM_MANAGER_H
#define ROOM_MANAGER_H

typedef struct list_s List;
typedef struct hashtable_s HashTable;
typedef struct room_s room;

struct room_manager {
    HashTable *rooms;
};

void room_manager_init();
void room_manager_add(int);
void room_manager_add_by_user_id(int);
List *room_manager_get_by_user_id(int);
room *room_manager_get_by_id(int);

#endif