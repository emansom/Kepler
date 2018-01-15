#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"

void GOTOFLAT(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    room_load(player->room_user->room, player);
}
