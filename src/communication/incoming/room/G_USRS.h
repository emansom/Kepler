#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_USRS(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char instance_id[11];
    sprintf(instance_id, "%i", player->player_data->id);
    instance_id[10] = '\0';

    char coords[33];
    sprintf(coords, "%i %i %f\0", player->room_user->room->room_data->model_data->door_x, player->room_user->room->room_data->model_data->door_y, player->room_user->room->room_data->model_data->door_z);

    outgoing_message *players = om_create(28); // "@\"
    om_write_str_kv(players, "i", "0");
    om_write_str_kv(players, "a", instance_id);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    om_write_str_kv(players, "f", coords);
    om_write_str_kv(players, "c", player->player_data->motto);
    player_send(player, players);
    om_cleanup(players);
}
