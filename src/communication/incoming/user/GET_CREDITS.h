#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void GET_CREDITS(player *player, incoming_message *message) {
    char credits_string[10 + 2 + 1]; ///"num + .0 + /0";
    sprintf(credits_string, "%i", player->player_data->credits);

    outgoing_message *credits = om_create(6); // "@F"
    om_write_str(credits, credits_string);
    player_send(player, credits);
    om_cleanup(credits);

}
