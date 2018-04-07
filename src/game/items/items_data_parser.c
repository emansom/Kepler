#include "stdio.h"
#include "stdlib.h"

#include "list.h"

#include "shared.h"

#include "item.h"
#include "items_data_parser.h"

/**
 * Parse the public room model items
 * @param model the model name to parse
 * @return the list of room items parsed
 */
List *item_parser_get_items(char *model) {
    char file_path[30];
    sprintf(file_path, "data/public_items/%s.dat", model);

    FILE *file = fopen(file_path, "r");

    if (!file) {
        return NULL;
    }

    List *items = NULL;
    list_new(&items);

    char * line = NULL;
    size_t len = 0;

    while ((getline(&line, &len, file)) != -1) {
        if (line == NULL) {
            continue;
        }

        char *str_x = get_argument(line, " ", 2);
        char *str_y = get_argument(line, " ", 3);
        char *str_z = get_argument(line, " ", 4);
        char *str_rotation = get_argument(line, " ", 5);
        char *public_custom_data = get_argument(line, " ", 6);

        item *room_item = item_create(
                -1,
                get_argument(line, " ", 1),
                -1,
                (int) strtol(str_x, NULL, 10),
                (int) strtol(str_y, NULL, 10),
                (int) strtol(str_z, NULL, 10),
                (int) strtol(str_rotation, NULL, 10),
                get_argument(line, " ", 0)
        );

        if (public_custom_data != NULL) {
            if (public_custom_data[0] == '2') {
                room_item->is_table = 1;
                free(public_custom_data);
            } else {
                room_item->current_program = public_custom_data;
            }
        }

        if (strstr(room_item->class_name, "chair") != NULL
            || strstr(room_item->class_name, "bench") != NULL
            || strstr(room_item->class_name, "seat") != NULL
            || strstr(room_item->class_name, "stool") != NULL
            || strstr(room_item->class_name, "sofa") != NULL
            || strcmp(room_item->class_name, "l") == 0
            || strcmp(room_item->class_name, "m") == 0
            || strcmp(room_item->class_name, "k") == 0
            || strcmp(room_item->class_name, "shift1") == 0) {
            room_item->can_sit = 1;
        } else {
            room_item->can_sit = 0;
            room_item->is_solid = 1;
        }

        if (strcmp(room_item->class_name, "poolLift") == 0
            || strcmp(room_item->class_name, "poolBooth") == 0) {
            room_item->can_sit = 0;
            room_item->is_solid = 0;
        }

        list_add(items, room_item);

        free(str_x);
        free(str_y);
        free(str_z);
        free(str_rotation);

        /*if (strcmp(room_item->class_name, "poolLift") == 0) {
            printf("extra data: %s\n", room_item->public_custom_data);
        }*/
    }

    fclose(file);

    /*if (strcmp(model, "cafe_ole") == 0) {
        file = fopen(file_path, "w+");

        for (int i = 0; i < list_size(items); i++) {
            item *room_item;
            list_get_at(items, i, (void*)&room_item);

            char custom_content[10];

            if (room_item->is_table) {
                strcpy(custom_content, " 2");
            } else {
                strcpy(custom_content, "");
            }

            char buf[100];
            if (strlen(custom_content) > 0) {
                sprintf(buf, "%s %s %i %i %i %i%s\n", room_item->custom_data, room_item->class_name, room_item->x,
                        room_item->y, (int) room_item->z, room_item->rotation, custom_content);
            } else {
                sprintf(buf, "%s %s %i %i %i %i\n", room_item->custom_data, room_item->class_name, room_item->x,
                        room_item->y, (int) room_item->z, room_item->rotation);
            }
            fputs(buf, file);
        }
        
        fclose(file);
    }*/

    return items;
}

