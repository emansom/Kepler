#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include <game/items/item.h>

#include <game/room/room_user.h>
#include <game/room/mapping/room_map.h>
#include <game/room/manager/room_item_manager.h>

#include <database/queries/player_query.h>
#include <database/queries/items/item_query.h>

#include "log.h"

void CONVERT_FURNI_TO_CREDITS(session *player, incoming_message *message) {
    if (player->room_user->room == NULL || !room_is_owner(player->room_user->room, player->player_data->id)) {
        return;
    }

    int item_id = im_read_vl64(message);

    if (item_id < 0) {
        return;
    }

    item *item = room_item_manager_get(player->room_user->room, item_id);

    if (item == NULL || !item->definition->behaviour->is_redeemable) {
        return;
    }

    char* str_amount = get_argument(item->definition->sprite, "_", 1);

    if (!is_numeric(str_amount)) {
        goto cleanup;
    }

    int amount = (int) strtol(str_amount, NULL, 10);

    room_map_remove_item(player->room_user->room, item);

    item_query_delete(item_id);
    item_dispose(item);

    player->player_data->credits += amount;
    session_send_credits(player);

    player_query_save_currency(player);

    cleanup:
    free(str_amount);
}