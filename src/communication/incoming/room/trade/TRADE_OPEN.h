#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/manager/room_trade_manager.h"

void TRADE_OPEN(entity *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    char *str_instance_id = get_argument(content, " ", 0);

    if (str_instance_id == NULL || !is_numeric(str_instance_id)) {
        goto cleanup;
    }

    int instance_id = (int)strtol(str_instance_id, NULL, 10);

    room_user *trade_user = room_user_get_by_instance_id(player->room_user->room, instance_id);

    if (trade_user == NULL) {
        goto cleanup;
    }

    trade_manager_reset(player->room_user);
    trade_manager_reset(trade_user);

    room_user_add_status(player->room_user, "trd", "", -1, "", -1, -1);
    player->room_user->needs_update = true;
    player->room_user->trade_partner = trade_user;

    room_user_add_status(trade_user, "trd", "", -1, "", -1, -1);
    trade_user->needs_update = true;
    trade_user->trade_partner = player->room_user;

    cleanup:
    free(content);
    free(str_instance_id);
}
