#include "lib/dyad/dyad.h"
#include "communication/messages/incoming_message.h"

void message_initcrypto(request message) {
    char *response = "DUIH\1";
    dyad_write(message.player->stream, response, strlen(response));

}