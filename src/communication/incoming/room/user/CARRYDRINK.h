#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"


void CARRYDRINK(session *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    int drink_id = (int) strtol(content, NULL, 10);

    if (drink_id >= 0 && drink_id <= 25) {
        room_user_carry_item(player->room_user, drink_id);
        room_user_reset_idle_timer(player->room_user);
    }
}