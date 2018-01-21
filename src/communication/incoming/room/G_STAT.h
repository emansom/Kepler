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

    player->room_user->needs_update = 1;
}
