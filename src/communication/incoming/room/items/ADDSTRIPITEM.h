#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/inventory/inventory.h"
#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/mapping/room_map.h"
#include "game/room/manager/room_item_manager.h"

#include "game/items/item.h"

void ADDSTRIPITEM(session *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    if (!room_has_rights(player->room_user->room, player->player_data->id)) {
        return;
    }

    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    char *remove_data_item_id = get_argument(content, " ", 2);

    if (remove_data_item_id == NULL) {
        goto cleanup;
    }

    item *item = room_item_manager_get(player->room_user->room, (int)strtol(remove_data_item_id, NULL, 10));

    if (item == NULL) {
        goto cleanup;
    }

    room_map_remove_item(player->room_user->room, item);

    inventory *inv = (inventory *) player->inventory;
    list_add(inv->items, item);
    inventory_send(inv, "update", player);

    cleanup:
        free(content);
        free(remove_data_item_id);
}
