#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"

void GOTOFLAT(player *player, incoming_message *message) {
    char *content = im_get_content(message);

    if (!is_numeric(content)) {
        free(content);
        return;
    }

    room *room = room_manager_get_by_id(strtol(content, NULL, 10));

    if (room != NULL) { 
        room_enter(room, player);
        room_load(room, player);
    }

    free(content);
}
