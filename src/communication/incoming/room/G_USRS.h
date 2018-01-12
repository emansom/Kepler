#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_USRS(player *player, incoming_message *message) {
    char var[11];
    sprintf(var, "%i", player->player_data->id);
    var[10] = '\0';

    outgoing_message *players = om_create(28); // "@\"
    om_write_str_kv(players, "i", "0");
    om_write_str_kv(players, "a", var);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    om_write_str_kv(players, "f", "3 5 0");
    om_write_str_kv(players, "c", player->player_data->motto);
    player_send(player, players);
    om_cleanup(players);
}
