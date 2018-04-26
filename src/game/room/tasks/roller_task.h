#ifndef ROLLER_TASK_H
#define ROLLER_TASK_H

#include "game/room/room.h"
#include "game/items/item.h"

void do_roller_task(room *room);
bool do_roller_item(room *room, item *roller, item *item);

#endif