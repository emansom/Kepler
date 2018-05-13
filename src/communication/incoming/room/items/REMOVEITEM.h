#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/inventory/inventory.h"
#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/mapping/room_map.h"

#include "game/items/item.h"

void REMOVEITEM(session *player, incoming_message *message) {
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

    int item_id = (int) strtol(content, NULL, 10);

    item *item = room_item_manager_get(player->room_user->room, item_id);

    if (item == NULL) {
        goto cleanup;
    }

    room_map_remove_item(player->room_user->room, item);

    item_query_delete(item_id);
    item_dispose(item);

    cleanup:
    free(content);
}
