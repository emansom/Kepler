#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void message_userinfo(player *player, incoming_message *message) {
    outgoing_message *om;

    om = om_create(5);
    om_write_str_int(om, player->player_data->id);
    om_write_str(om, player->player_data->username);
    om_write_str(om, player->player_data->figure);
    om_write_str(om, player->player_data->sex);
    om_write_str(om, player->player_data->motto);
    om_write_int(om, player->player_data->tickets);
    om_write_str(om, ""); // pool figure
    om_write_int(om, player->player_data->film);
    player_send(player, om);
    om_cleanup(om);

}
