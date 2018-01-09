#include <communication/messages/incoming_message.h>
#include "communication/messages/incoming_message.h"

void message_versioncheck(request message) {
    char *response = "###SECRET_KEY\r1337##";
    dyad_write(message.stream, response, strlen(response));

}