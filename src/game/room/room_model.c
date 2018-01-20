#include "stdlib.h"
#include <ctype.h>

#include "room_model.h"
#include "shared.h"
#include "list.h"

#include "game/items/item.h"
#include "game/navigator/navigator_category_manager.h"

#include "game/items/items_data_parser.h"

/**
 *
 * @param modeltype
 * @param door_x
 * @param door_y
 * @param door_z
 * @param heightmap
 * @return
 */
room_model *room_model_create(char *model_id, char *model_name, int door_x, int door_y, double door_z, int door_dir, char *heightmap) {
    room_model *model = malloc(sizeof(room_model));
    model->model_id = strdup(model_id);
    model->model_name = strdup(model_name);
    model->door_x = door_x;
    model->door_y = door_y;
    model->door_z = door_z;
    model->door_dir = door_dir;
    model->map_size_x = 0;
    model->map_size_y = 0;
    model->heightmap = replace(heightmap, '|', "\r");
    
    List *items = item_parser_get_items(model->model_id);

    if (items != NULL) {
        model->public_items = items;
    } else {
        list_new(&model->public_items);
    }
    
    room_model_parse(model);
    return model;
}

void room_model_parse(room_model *room_model) {
    char *heightmap = strdup(room_model->heightmap);
    char *array[100];

    int lines = 0;

    array[lines] = strtok(heightmap,"\r");

    while(array[lines]!=NULL) {
        array[++lines] = strtok(NULL,"\r");
    }

    int map_size_x = strlen(array[0]);
    int map_size_y = lines;

    room_model->map_size_x = map_size_x;
    room_model->map_size_y = map_size_y;

    room_model->states = malloc(sizeof(*room_model->states) * map_size_x);
    room_model->heights = malloc(sizeof(*room_model->heights) * map_size_x);

    for (int x = 0; x < map_size_x ; x++) { 
         room_model->states[x] = malloc(sizeof(*room_model->states) * map_size_y);
         room_model->heights[x] = malloc(sizeof(*room_model->heights) * map_size_y);
    }


     for (int y = 0; y < map_size_y; y++) {
        char *line = array[y];

        for (int x = 0; x < strlen(line); x++) {
            char ch = (char)tolower(line[x]);

            if (ch == 'x') {
                room_model->states[x][y] = CLOSED;
                room_model->heights[x][y] = 0;
            } else {
                int height = ch - '0';
                room_model->states[x][y] = OPEN;
                room_model->heights[x][y] = height;
            }
            
            if (x == room_model->door_x && y == room_model->door_y) {
                room_model->states[x][y] = OPEN;
            }
        }
     }

    free(heightmap);
}