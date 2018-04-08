#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

void WALK(player *player, incoming_message *im) {
    if (player->room_user->room == NULL) {
        return;
    }

    int x = im_read_b64_int(im);
    int y = im_read_b64_int(im);
    
    walk_to((room_user*) player->room_user, x, y);
}
