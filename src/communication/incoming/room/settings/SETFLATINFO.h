#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/room/room.h"

#include "database/queries/room_query.h"

void SETFLATINFO(player *player, incoming_message *message) {
	char *content = im_get_content(message);
	char *argument = get_argument(content, "/", 0);

	if (!is_numeric(argument)) {
		goto cleanup;
		return;
	}

	room *room = room_manager_get_by_id(strtol(argument, NULL, 10));

	if (room == NULL) {
		goto cleanup;
		return;
	}

	int split_count = 0;

	char *copy = strdup(content);
	char *token;

    for (char *token = strtok(copy, "\r"); token; token = strtok(NULL, "\r")) {
        split_count++;
	}

	for (int i = 1; i < split_count; i++) {
		char *setting_data = get_argument(content, "\r", i);
		char *key = get_argument(setting_data, "=", 0);
		
		char *value = setting_data;
		value += strlen(key) + 1;
		value = strdup(value); // create its own independent pointer

		if (strcmp(key, "description") == 0) {
			free(room->room_data->description);
			room->room_data->description = strdup(value);

		} else if (strcmp(key, "allsuperuser") == 0) {
			if (is_numeric(value)) {
				int allsuperuser = strtol(value, NULL, 10);
				room->room_data->superusers = (allsuperuser == 1);
			}

		} else if (strcmp(key, "maxvisitors") == 0) {
			if (is_numeric(value)) {
				int max_visitors = strtol(value, NULL, 10);
			
				if (max_visitors < 10 || max_visitors > 50) {
					max_visitors = 25;
				}

				room->room_data->visitors_max = max_visitors;
			}
		} else if (strcmp(key, "password") == 0) {
			free(room->room_data->password);
			room->room_data->password = strdup(value);

		}

		free(setting_data);
		free(key);
		free(value);
	}

	query_room_save(room);

	cleanup:
		free(argument);
		free(copy);
		free(content);
}