#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void GETINTERST(player *player, incoming_message *message) {
    outgoing_message *om = om_create(258); // "DB"
    om_write_str_int(om, 0);
    player_send(player, om);
    om_cleanup(om);
}
