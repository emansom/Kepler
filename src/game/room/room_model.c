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

    int count = 0;
    char *line = get_argument(heightmap, "\r", count);

    int map_size_y = strlen(line);
    int map_size_x = 0;

    room_title_states temp[100][100];
    memset(temp, -1, sizeof(temp));

    while (line != NULL) {
        for (int i = 0; i < strlen(line); i++) {
            char letter = (char)tolower(line[i]);

            if (letter == 'x') {
                temp[count][i] = CLOSED;
            } else {
                temp[count][i] = OPEN;
            }   
        }
    
        if (line != NULL) {
            free(line);
        }  

        count++;
        map_size_x = count;

        line = get_argument(heightmap, "\r", count);
    }

    printf("Model: %s\n", room_model->model_name);
    printf("x: %d y: %d\n", map_size_x, map_size_y);

    room_model->map_size_x = map_size_x;
    room_model->map_size_y = map_size_y;
    room_model->states = malloc(sizeof(*room_model->states) * map_size_x);

    for (int x = 0; x < room_model->map_size_x ; x++) { 
         room_model->states[x] = malloc(sizeof(*room_model->states) * map_size_y);

         for (int y = 0; y < room_model->map_size_y ; y++) { 
             room_model->states[x][y] = temp[x][y];
         }
    }

    free(heightmap);
}