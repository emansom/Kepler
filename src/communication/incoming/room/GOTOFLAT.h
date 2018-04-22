#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"

void GOTOFLAT(session *player, incoming_message *message) {
    char *content = im_get_content(message);

    if (!is_numeric(content)) {
        goto cleanup;
    }

    room *room = room_manager_get_by_id((int)strtol(content, NULL, 10));

    if (room == NULL) {
        goto cleanup;
    }

    if (player->room_user->authenticate_id != room->room_id) {
        goto cleanup;
    }

    room_enter(room, player);
    room_load(room, player);

    cleanup:
        free(content);
}
