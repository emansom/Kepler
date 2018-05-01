#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"

#include "util/stringbuilder.h"
#include "database/queries/rooms/room_favourites_query.h"

void GETFVRF(session *player, incoming_message *message) {
    List *favourite_rooms = room_query_favourites(player->player_data->id);

    outgoing_message *om = om_create(61); // "@}"
    sb_add_string(om->sb, "HHJ\2HHH");
    om_write_int(om, (int) list_size(favourite_rooms));

    for (size_t i = 0; i < list_size(favourite_rooms); i++) {
        room *room;
        list_get_at(favourite_rooms, i, (void *) &room);
        room_append_data(room, om, player->player_data->id);
    }

    player_send(player, om);
    om_cleanup(om);

    list_destroy(favourite_rooms);
}