#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void GET_CREDITS(player *player, incoming_message *message) {
    player_send_credits(player);
}
