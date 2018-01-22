#ifndef ITEM_H
#define ITEM_H

typedef struct item_s {
	char *class_name;
	int is_table;
	int sprite_id;
	int x;
	int y;
	double z;
	int rotation;
	char *custom_data;
	int can_sit;
	int is_solid;
} item;

item *item_create(char*, int, int, int, double, int, char*);

#endif