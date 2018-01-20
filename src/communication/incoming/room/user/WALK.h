#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

void WALK(player *player, incoming_message *im) {
    if (player->room_user->room == NULL) {
        return;
    }

    walk_to(player->room_user, im_read_vl64(im), im_read_vl64(im));
}
