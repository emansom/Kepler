#ifndef ROOM_QUERY_H
#define ROOM_QUERY_H

typedef struct list_s List;
typedef struct hashtable_s HashTable;

List *room_query_get_by_id(int);
void room_query_get_models();
void room_query_get_categories();

#endif