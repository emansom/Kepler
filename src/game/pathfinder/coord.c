#include "coord.h"

int distance_squared(int first_x, int first_y, int point_x, int point_y) {
	int dx = first_x - point_x;
	int dy = first_y - point_y;
	return (dx * dx) + (dy * dy);
}