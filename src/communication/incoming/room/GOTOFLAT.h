#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void GOTOFLAT(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    player->room_user->room->room_data->visitors_now++;

    outgoing_message *om = om_create(166); // "Bf"
    om_write_str(om, "/client/");
    player_send(player, om);
    om_cleanup(om);

    om = om_create(69); // "AE"
    om_write_str(om, player->room_user->room->room_data->model);
    om_write_str(om, " ");
    om_write_str_int(om, player->room_user->room_id);
    player_send(player, om);
    om_cleanup(om);

    om = om_create(345); // "EY"
    om_write_int(om, 0); // votes
    player_send(player, om);
    om_cleanup(om);
}
