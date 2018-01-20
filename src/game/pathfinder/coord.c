#include "coord.h"
#include "stdlib.h"

int distance_squared(int first_x, int first_y, int point_x, int point_y) {
	int dx = first_x - point_x;
	int dy = first_y - point_y;
	return (dx * dx) + (dy * dy);
}

coord *create_coord(int x, int y) {
	coord *pos = malloc(sizeof(coord));
	pos->x = x;
	pos->y = y;
	pos->z = 0;
	return pos;
}