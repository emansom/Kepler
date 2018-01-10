#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void message_userinfo(player *player, incoming_message *message) {
    outgoing_message *om;

    om = om_create(5);
    player_send(player, om);

}
