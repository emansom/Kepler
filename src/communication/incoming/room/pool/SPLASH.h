#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"
#include "game/room/pool/pool_handler.h"

void SPLASH(player *player, incoming_message *message) {
    printf("Handled: %s\n", im_get_content(message));
}
