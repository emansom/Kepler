#ifndef ROOM_MODEL_H
#define ROOM_MODEL_H

typedef struct list_s List;

typedef struct room_model_s {
    char *model_id;
    char *model_name;
    int door_x;
    int door_y;
    double door_z;
    char *heightmap;
    List *public_items;
} room_model;

room_model *room_model_create(char*, char *, int, int, double, char *);

#endif