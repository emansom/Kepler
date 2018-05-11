#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/club/club_manager.h"

void GET_CLUB(session *player, incoming_message *message) {
    club_refresh(player);
}