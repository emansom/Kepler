#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void GET_CREDITS(session *player, incoming_message *message) {
    player_refresh_credits(player);
}
