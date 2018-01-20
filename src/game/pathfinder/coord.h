#ifndef COORD_H
#define COORD_H

typedef struct coord_s {
	int x;
	int y;
	double z;
} coord;

coord *create_coord(int, int);
int distance_squared(int first_x, int first_y, int point_x, int point_y);

#endif