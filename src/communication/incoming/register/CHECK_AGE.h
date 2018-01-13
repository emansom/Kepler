#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void CHECK_AGE(player *player, incoming_message *message) {
    outgoing_message *om = om_create(271); // "DO"
    //outgoing_message *om = om_create(272); // "DP"
    om_write_str_int(om, 1);
    player_send(player, om);
    om_cleanup(om);
}
