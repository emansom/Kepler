#include "lib/dyad/dyad.h"
#include "communication/messages/incoming_message.h"
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void message_initcrypto(player *player, incoming_message *message) {
    outgoing_message *om;

    om = om_create(277);
    om_write_int(om, 1);
    om_write_int(om, 0);
    player_send(player, om);
    om_cleanup(om);
}