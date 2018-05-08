#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void GET_SONG_INFO(session *player, incoming_message *message) {
    outgoing_message *om;

    om = om_create(301); // "
    sb_add_string(om->sb, "PAPAIKQBSDPEQERESEPFQFRFSFJQAQBQIRISIPJQJRJSJPKQKKIQBIJKPAQARASAPBQBPARAQBRKSKPLQLRLSLPMQMRMDl");
    om_write_str(om, "sound_set 1");
    player_send(player, om);
    om_cleanup(om);

    om = om_create(302); // "Dl"
    om_write_int(om, 3);
    om_write_int(om, 1);
    om_write_int(om, 2);
    om_write_int(om, 3);
    player_send(player, om);
    om_cleanup(om);
}
