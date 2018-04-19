#ifndef ROOM_ITEM_MANAGER_H
#define ROOM_ITEM_MANAGER_H

typedef struct room_s room;
typedef struct item_s item;

void room_item_manager_load(room *room);
void room_item_manager_dispose(room *room);

#endif