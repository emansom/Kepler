#include "coord.h"

#include "stdio.h"
#include "stdlib.h"

/**
 * Create a coordinate struct with given default parameters
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * 
 * @return coord ptr
 */
coord *create_coord(int x, int y) {
    coord *pos = malloc(sizeof(coord));
    pos->x = x;
    pos->y = y;
    pos->z = 0;
    pos->head_rotation = 0;
    pos->body_rotation = 0;
    pos->rotation = 0;
    return pos;
}

/**
 * Get the distance squared between two points.
 * 
 * @param first the first coordinate to compare
 * @param second the second coordinate to compare
 * 
 * @return distance
 */
int distance_squared(coord first, coord second) {
    int dx = first.x - second.x;
    int dy = first.y - second.y;
    return (dx * dx) + (dy * dy);
}

void coord_set_rotation(coord *coord, int head_rotation, int body_rotation) {
    coord->rotation = body_rotation;
    coord->body_rotation = body_rotation;
    coord->head_rotation = head_rotation;
}

void coord_get_front(coord *start_coord, coord *new_coord) {

}