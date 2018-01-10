#include "lib/dyad/dyad.h"
#include "communication/messages/incoming_message.h"

void message_initcrypto(player *player, incoming_message *message) {
    char *response = "DUIH\1";
    dyad_write(player->stream, response, strlen(response));

}