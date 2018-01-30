#ifndef ROOM_QUERY_H
#define ROOM_QUERY_H

typedef struct list_s List;
typedef struct hashtable_s HashTable;
typedef struct room_s room;

List *room_query_get_by_owner_id(int);
room *room_query_get_by_room_id(int);

void room_query_get_models();
void room_query_get_categories();
void room_query_save(int, char*, int, int, char*);

#endif