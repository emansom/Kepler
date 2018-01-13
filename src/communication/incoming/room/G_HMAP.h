#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_HMAP(player *player, incoming_message *message) {
    char *heightmap = "xxxxxxxxxxxx\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxx00000000\rxxxxxxxxxxxx\rxxxxxxxxxxxx\r";
    outgoing_message *om = om_create(31); // "@_"
    om_write_str(om, heightmap);
    player_send(player, om);
    om_cleanup(om);
}
