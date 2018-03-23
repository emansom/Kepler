#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void SETFLATCAT(player *player, incoming_message *message) {
    int room_id = im_read_vl64(message);
    int category_id = im_read_vl64(message);

    room_category *category = category_manager_get_by_id(category_id);

    if (category == NULL) {
        return;
    }

    if (category->public_spaces) {
        return;
    }

    room *room = room_manager_get_by_id(room_id);

    if (room == NULL) {
        return;
    }

    if (room->room_data->owner_id != player->player_data->id) {
        return;
    }

    if (player->player_data->rank >= category->minrole_setflatcat) {
        room->room_data->category = category_id;
        query_room_save(room);
    }
}