#include "stdlib.h"

#include "items.h"

item *item_create(char *class_name, int sprite_id, int x, int y, double z, int rotation) {
	item *room_item = malloc(sizeof(item));
	room_item->class_name = class_name;
	room_item->sprite_id = sprite_id;
	room_item->x = x;
	room_item->y = y;
	room_item->z = z;
	room_item->rotation = rotation;
	return room_item;
}