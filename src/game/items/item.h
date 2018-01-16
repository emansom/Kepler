#ifndef ITEM_H
#define ITEM_H

typedef struct item_s {
	char *class_name;
	int sprite_id;
	int x;
	int y;
	double z;
	int rotation;
	char *custom_data;
} item;

item *item_create(char*, int, int, int, double, int, char*);

#endif