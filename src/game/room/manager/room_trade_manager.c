#include <shared.h>
#include <game/player/player.h>

#include "game/items/item.h"
#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/room/manager/room_trade_manager.h"
#include "communication/messages/outgoing_message.h"

void trade_manager_reset(room_user *room_user) {
    memset(room_user->trade_items, -1, sizeof(room_user->trade_items));

    room_user->trade_item_count = 0;
    room_user->trade_partner = NULL;
    room_user->trade_accept = false;

    room_user_remove_status(room_user, "trd");
}

void trade_manager_reset_boxes(room_user *room_user) {
    outgoing_message *om = om_create(108); // "Al"
    om_write_str(om, room_user->entity->details->username);
    om_write_int(om, room_user->trade_accept);

    if (room_user->trade_item_count > 0) {

    }

    om_write_char(om, 13);

}