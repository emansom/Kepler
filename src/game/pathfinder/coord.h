#ifndef COORD_H
#define COORD_H

typedef struct coord_s {
    int x;
    int y;
    double z;
    int head_rotation;
    int body_rotation;
    int rotation;
} coord;

coord *create_coord(int x, int y);
coord *create_coord_height(int x, int y, double z);
int distance_squared(coord from, coord to);
void coord_set_rotation(coord *coord, int head_rotation, int body_rotation);
void coord_get_front(coord *start_coord, coord *new_coord);

#endif