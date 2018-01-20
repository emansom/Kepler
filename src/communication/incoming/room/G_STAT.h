#include <game/room/room_model.h>
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_STAT(player *p, incoming_message *message) {
    if (p->room_user->room == NULL) {
        return;
    }

    if (p->room_user->room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", p->room_user->room->room_data->id);
        return;
    }

    p->room_user->needs_update = 1;
}
