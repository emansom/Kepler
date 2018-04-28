#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/inventory/inventory.h"
#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/mapping/room_map.h"

#include "game/items/item.h"

void SETSTUFFDATA(session *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    if (!room_is_owner(player->room_user->room, player->player_data->id)) {
        return;
    }

    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    printf("Set stuff data called..\n");

    cleanup:
        free(content);
        
}
