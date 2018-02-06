#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void MESSENGERINIT(player *player, incoming_message *message) {
    if (player->player_data == NULL) {
        return;
    }
    
    outgoing_message *response = om_create(12); // "@L"
    om_write_str(response, player->player_data->console_motto);
    om_write_int(response, 600);
    om_write_int(response, 200);
    om_write_int(response, 600);
    om_write_int(response, 0); // Buddy list count
    player_send(player, response);
    om_cleanup(response);

}
