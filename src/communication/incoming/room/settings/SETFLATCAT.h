#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void SETFLATCAT(player *player, incoming_message *message) {
    int room_id = im_read_vl64(message);
	int category_id = im_read_vl64(message);

	room_category *category = category_manager_get_by_id(category_id);

	if (category->public_spaces) {
		printf("failed 1\n");
		return;
	}

	room *room = room_manager_get_by_id(room_id);

	if (room == NULL) {
		printf("failed 2\n");
		return;
	}

	if (room->room_data->owner_id != player->player_data->id) {
		printf("failed 3\n");
		return;
	}

	room->room_data->category = category_id;
	query_room_save(room);
}