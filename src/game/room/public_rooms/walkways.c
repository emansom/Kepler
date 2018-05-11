#include <game/room/room_user.h>
#include "stdlib.h"

#include "hashtable.h"
#include "list.h"

#include "game/pathfinder/coord.h"

#include "walkways.h"
#include "shared.h"

void walkways_init() {
    hashtable_new(&global.walkway_manager.walkways);
    walkways_add("rooftop", "rooftop_2", "9,4 10,3 9,3", NULL);
    walkways_add("rooftop_2", "rooftop", "3,10 4,10 5,10 3,11 4,11 5,11", "10,5,4,4");
}

void walkways_add(char *model_from, char *model_to, char *from_coords, char *destination) {
    walkway_entrance *walkway = malloc(sizeof(walkway_entrance));
    walkway->model_from = strdup(model_from);
    walkway->model_to = strdup(model_to);
    walkway->from_coords = walkways_get_coords(from_coords);

    if (destination != NULL) {
        walkway->destination = coord_create(0, 0);

        char *str_x = get_argument(destination, ",", 0);
        char *str_y = get_argument(destination, ",", 1);
        char *str_z = get_argument(destination, ",", 2);
        char *str_r = get_argument(destination, ",", 3);

        walkway->destination->x = (int) strtol(str_x, NULL, 10);
        walkway->destination->y = (int) strtol(str_y, NULL, 10);
        walkway->destination->z = (int) strtol(str_z, NULL, 10);
        walkway->destination->head_rotation = (int) strtol(str_r, NULL, 10);
        walkway->destination->body_rotation = (int) strtol(str_r, NULL, 10);

        free(str_x);
        free(str_y);
        free(str_z);
        free(str_r);

    } else {
        walkway->destination = NULL;
    }

    hashtable_add(global.walkway_manager.walkways, walkway->model_from, walkway);
}

walkway_entrance *walkways_activated(room_user *room_user) {
    HashTableIter iter;
    hashtable_iter_init(&iter, global.walkway_manager.walkways);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        walkway_entrance *entrance = entry->value;

        if (strcmp(room_user->room->room_data->model, entrance->model_from) == 0) {
            for (size_t i = 0; i < list_size(entrance->from_coords); i++) {
                coord *coord;
                list_get_at(entrance->from_coords, i, (void *) &coord);

                if (coord->x == room_user->position->x && coord->y == room_user->position->y) {
                    return entrance;
                }
            }
        }
    }

    return NULL;
}

room *walkways_find_room(char *model) {
    if (hashtable_size(global.room_manager.rooms) == 0) {
        return NULL;
    }

    HashTableIter iter;
    hashtable_iter_init(&iter, global.room_manager.rooms);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        room *room = entry->value;

        if (strcmp(room->room_data->model, model) == 0) {
            return room;
        }
    }

    return NULL;
}

List *walkways_get_coords(char *coords) {
    List *coordinates;
    list_new(&coordinates);

    for (int i = 0; i < 6; i++) {
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