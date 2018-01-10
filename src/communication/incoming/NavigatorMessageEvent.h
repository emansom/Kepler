#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void message_navigator(player *player, incoming_message *message) {
    printf("1: %i\n", im_read_vl64(message));
    printf("2: %i\n", im_read_vl64(message));
    printf("3: %i\n", im_read_vl64(message));
}
