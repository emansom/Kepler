#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

#include "game/room/room.h"

#include "thpool.h"

void WAVE(session *player, incoming_message *im) {
    if (player->room_user->room == NULL) {
        return;
    }

    if (!room_user_has_status(player->room_user, "wave")) {
        room_user_add_status(player->room_user, "wave", "", 2, "", 0, 0);
        player->room_user->needs_update = 1;
    }

    /*if (!room_user_has_status(session->room_user, "wave")) {
        thpool_add_work(global.thread_manager.pool, (void*)wave_task, session->room_user);
    }*/
}
