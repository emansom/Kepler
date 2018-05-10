#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void GET_INFO(session *player, incoming_message *message) {
    outgoing_message *user_info = om_create(5);
    om_write_str_int(user_info, player->player_data->id);
    om_write_str(user_info, player->player_data->username);
    om_write_str(user_info, player->player_data->figure);
    om_write_str(user_info, player->player_data->sex);
    om_write_str(user_info, player->player_data->motto);
    om_write_int(user_info, player->player_data->tickets);
    om_write_str(user_info, player->player_data->pool_figure); // pool figure
    om_write_int(user_info, player->player_data->film);
    player_send(player, user_info);
    om_cleanup(user_info);

}
