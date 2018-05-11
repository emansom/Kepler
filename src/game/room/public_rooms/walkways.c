#include "stdlib.h"

#include "hashtable.h"
#include "list.h"

#include "game/pathfinder/coord.h"

#include "walkways.h"
#include "shared.h"

void walkways_init() {
    hashtable_new(&global.walkway_manager.walkways);
    walkways_add("rooftop", "rooftop2", "9,4 10,3 9,3", "1,0");
}

void walkways_add(char *model_from, char *model_to, char *from_coords, char *destination_coords) {
    walkway_entrance *walkway = malloc(sizeof(walkway_entrance));
    walkway->model_from = strdup(model_from);
    walkway->model_to = strdup(model_to);
    walkway->from_coords = walkways_get_coords(from_coords);
    walkway->destination_coords = walkways_get_coords(destination_coords);

    hashtable_add(global.walkway_manager.walkways, walkway->model_from, walkway);
}

List *walkways_get_coords(char *coords) {
    List *coordinates;
    list_new(&coordinates);

    for (int i = 0; i < 4; i++) {
        char *coord = get_argument(coords, " ", i);

        if (coord == NULL) {
            continue;
        }

        char *str_x = get_argument(coord, ",", 0);
        char *str_y = get_argument(coord, ",", 1);

        int x = (int) strtol(str_x, NULL, 10);
        int y = (int) strtol(str_y, NULL, 10);

        list_add(coordinates, coord_create(x, y));

        free(coord);
        free(str_x);
        free(str_y);
    }

    return coordinates;
}