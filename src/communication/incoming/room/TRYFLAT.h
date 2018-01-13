#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room_user.h"
#include "game/room/room_manager.h"

void TRYFLAT(player *player, incoming_message *message) {
    char *content = im_get_content(message);

    if (is_numeric(content)) {
        player->room_user->room_id = atoi(content);
        player->room_user->room = room_manager_get_by_id(player->room_user->room_id);

        outgoing_message *interest = om_create(41); // "@i"
        player_send(player, interest);
        om_cleanup(interest);
    }

    free(content);
}
