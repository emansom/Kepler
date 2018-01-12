#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_USRS(player *player, incoming_message *message) {
    outgoing_message *interest = om_create(28); // "@\"
    player_send(player, interest);
    om_cleanup(interest);
}
