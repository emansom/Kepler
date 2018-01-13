#include <game/room/room.h>
#include "util/stringbuilder.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room_manager.h"
#include "list.h"

void SUSERF(player *player, incoming_message *message) {
    List *rooms = room_manager_get_by_user_id(player->player_data->id);
    room *room;

    ListIter iter;
    list_iter_init(&iter, rooms);

    outgoing_message *om = om_create(16); // "@P"
    while (list_iter_next(&iter, (void*) &room) != CC_ITER_END) {
        om_write_int_delimeter(om, room->room_id, 9);
        om_write_str_delimeter(om, room->room_data->name, 9);
        om_write_str_delimeter(om, room->room_data->owner_name, 9);
        om_write_str_delimeter(om, "open", 9);
        om_write_str_delimeter(om, "x", 9);
        om_write_int_delimeter(om, room->room_data->visitors_now, 9);
        om_write_int_delimeter(om, room->room_data->visitors_max, 9);
        om_write_str_delimeter(om, "null", 9);
        om_write_str_delimeter(om, room->room_data->description, 9);
        om_write_str_delimeter(om, room->room_data->description, 9);
        om_write_char(om, 13);
    }

    player_send(player, om);
    om_cleanup(om);
    list_destroy(rooms);
}
