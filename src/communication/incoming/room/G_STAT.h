#include <game/room/room_model.h>
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_STAT(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    if (player->room_user->room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", player->room_user->room->room_data->id);
        return;
    }

    outgoing_message *status_update = om_create(34);
	append_user_status(status_update, player);
	room_send(player->room_user->room, status_update);
}
