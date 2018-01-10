#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void message_login(request message) {
    char *username = im_read_str(message.im);
    char *password = im_read_str(message.im);

    printf("Username: %s and password: %s\n", username, password);

    outgoing_message *om = om_create(12);

    free(username);
    free(password);


}
