#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void room_directory(player *player, incoming_message *message) {
    outgoing_message *om = om_create(19); // "@S"
    player_send(player, om);
    om_cleanup(om);

    /*om = om_create(166); // "Bf"
    om_write_raw_str(om, "/client/");
    player_send(player, om);
    om_cleanup(om);*/
}
