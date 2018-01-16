#ifndef ITEMS_H
#define ITEMS_H

typedef struct item_s {
	char *class_name;
	int sprite_id;
	int x;
	int y;
	double z;
	int rotation;
} item;

item *item_create(char*, int, int, int, double, int);

#endif