#include "list.h"

#include "affected_tiles.h"
#include "coord.h"

List *get_affected_tiles(int item_length, int item_width, int x, int y, int rotation) {
    List *tiles;
    list_new(&tiles);

    if (item_length != item_width) {
        if (rotation == 0 || rotation == 4) {
            int l = item_length;
            int w = item_width;
            item_length = w;
            item_width = l;
        }
    }

    for (int newX = x; newX < x + item_width; newX++) {
        for (int newY = y; newY < y + item_length; newY++) {
            coord *coord = create_coord(newX, newY);
            list_add(tiles, coord);
        }
    }

    return tiles;
}