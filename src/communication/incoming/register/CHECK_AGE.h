#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void CHECK_AGE(player *player, incoming_message *message) {
    outgoing_message *om = om_create(271); // "DO"
    //outgoing_message *om = om_create(272); // "DP"
    player_send(player, om);
    om_cleanup(om);
}
