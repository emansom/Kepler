#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/room/room.h"

#include "database/queries/room_user_query.h"

void SETFLATINFO(player *player, incoming_message *message) {
	int room_id = 0;

	char *content = im_get_content(message);
	room_id = strtol(get_argument(content, "/", 0), NULL, 10);

	printf("received room id: %i\n", room_id);

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

		printf("k: %s and v: %s\n", key, value);

		free(setting_data);
		free(key);
		free(value);
	}

	free(copy);
	free(content);
}