#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/room/tasks/wave_task.h"

#include "thpool.h"

void WAVE(player *player, incoming_message *im) {
    if (player->room_user->room == NULL) {
        return;
    }

    if (!room_user_has_status(player->room_user, "wave")) {
        thpool_add_work(global.thread_manager.pool, (void*)wave_task, player->room_user);
    } else {
        printf("Wave prevented\n");
    }
}
