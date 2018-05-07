#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void PONG(session *player, incoming_message *message) {
    player->ping_safe = true;
}