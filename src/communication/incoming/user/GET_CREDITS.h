#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void GET_CREDITS(session *player, incoming_message *message) {
    session_send_credits(player);
}
