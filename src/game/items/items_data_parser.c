#include "stdio.h"
#include "stdlib.h"

#include "list.h"

#include "shared.h"

#include "items.h"
#include "items_data_parser.h"

List *item_parser_get_items(char *model) {
	char file_path[30];
	sprintf(file_path, "data/public_items/%s.dat", model);

	FILE *file = fopen(file_path, "r");

	if (!file) {
		return NULL;
	}

	List *items;
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

		item *room_item = item_create(
			get_argument(line, " ", 1),
			-1,
			strtol(str_x, NULL, 10),
			strtol(str_y, NULL, 10),
			strtol(str_z, NULL, 10),
			strtol(str_rotation, NULL, 10)
		);

		free(str_x);
		free(str_y);
		free(str_z);
		free(str_rotation);
	}	

	fclose(file);
	return items;
}
