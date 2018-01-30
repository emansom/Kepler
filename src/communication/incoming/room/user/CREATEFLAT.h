#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/room/room.h"

#include "database/queries/room_user_query.h"

void CREATEFLAT(player *player, incoming_message *message) {
	// Client [0.0.0.0] incoming data: 29 / @]/first floor/xddd/model_a/open/1
	char *content = im_get_content(message);
	char *floor_setting = get_argument(content, "/", 0);
	char *room_name = get_argument(content, "/", 1);
	char *room_select_model = get_argument(content, "/", 2);
	char *room_setting = get_argument(content, "/", 3);
	char *room_show_name = get_argument(content, "/", 4);

	if (strcmp(floor_setting, "first floor") != 0) {
		goto cleanup;
		return;
	}

	if (strcmp(room_name, "pepe the frog") == 0) {
		goto cleanup;
		return;
	}

	room_model *model = model_manager_get(room_select_model);

	if (model == NULL) {
		goto cleanup;
		return;
	}

	if (list_size(model->public_items) > 0) { // model is a public room model
		goto cleanup;
		return;
	}

	// TODO: Check model permissions
	printf("Room creation success.\n");

	int room_id = query_room_create(room_name, room_select_model, room_setting, room_show_name);
	printf("room id: %i\n", room_id);

	cleanup:
		free(floor_setting);
		free(room_name);
		free(room_select_model);
		free(room_setting);
		free(room_show_name);
		free(content);
}


