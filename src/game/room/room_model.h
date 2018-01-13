#ifndef ROOM_MODEL_H
#define ROOM_MODEL_H

typedef struct room_model_s {
    int door_x;
    int door_y;
    double door_z;
    char *heightmap;
} room_model;

room_model *room_model_create(int, int, double, char *);

#endif