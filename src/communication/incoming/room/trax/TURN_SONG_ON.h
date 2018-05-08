#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void TURN_SONG_ON(session *player, incoming_message *message) {
    outgoing_message *om = om_create(300); // "Dl"
    om_write_str(om, "sound_set 1");
    om_write_int(om, 72);
    player_send(player, om);
    om_cleanup(om);

    
}
