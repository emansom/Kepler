#ifndef COORD_H
#define COORD_H

typedef struct coord_s {
	int x;
	int y;
	double z;
} coord;

coord *create_coord(int, int);
int distance_squared(coord, coord);

#endif