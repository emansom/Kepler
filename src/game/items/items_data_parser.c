#include "stdio.h"
#include "stdlib.h"

#include "list.h"

#include "shared.h"

#include "item.h"
#include "items_data_parser.h"

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
    size_t read;

	while ((read = getline(&line, &len, file)) != -1) {
		if (line == NULL) {
			continue;
		}

		char *str_x = get_argument(line, " ", 2);
		char *str_y = get_argument(line, " ", 3);
		char *str_z = get_argument(line, " ", 4);
		char *str_rotation = get_argument(line, " ", 5);
		char *str_table = get_argument(line, " ", 6);


		item *room_item = item_create(
			get_argument(line, " ", 1),
			-1,
			strtol(str_x, NULL, 10),
			strtol(str_y, NULL, 10),
			strtol(str_z, NULL, 10),
			strtol(str_rotation, NULL, 10),
			get_argument(line, " ", 0)
		);

		/*if (str_table != NULL) {
			if (str_table[0] == '2' && strcmp(model, "cafe_gold0") != 0 && strcmp(model, "ballroom") != 0 && strcmp(model, "library") != 0) {
				room_item->is_table = 1; 
			}

			free(str_table);
		}*/

		if (strstr(room_item->class_name, "chair") != NULL || strstr(room_item->class_name, "bench") != NULL || strstr(room_item->class_name, "seat") != NULL || strstr(room_item->class_name, "stool") != NULL || strstr(room_item->class_name, "sofa") != NULL) {
			room_item->can_sit = 1;
		} else {
			room_item->can_sit = 0;
			room_item->is_solid = 1;
		}

		list_add(items, room_item);

		free(str_x);
		free(str_y);
		free(str_z);
		free(str_rotation);
	}

	fclose(file);

	/*file = fopen(file_path, "w+");

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
		sprintf(buf, "%s %s %i %i %i %i%s\n", room_item->custom_data, room_item->class_name, room_item->x, room_item->y, (int)room_item->z, room_item->rotation, custom_content);
		fputs(buf, file);
	}
	
	fclose(file);*/

	return items;
}

