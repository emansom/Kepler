#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_OBJS(player *player, incoming_message *message) {
    outgoing_message *om = om_create(30); // "@^"
    player_send(player, om);
    om_cleanup(om);

    om = om_create(32); // "@`"
    om_write_int(om, 0);
    player_send(player, om);
    om_cleanup(om);

    om = om_create(297); // "Di"
    om_write_int(om, 0);
    player_send(player, om);
    om_cleanup(om);

    om = om_create(309); // "Du"
    om_write_int(om, 0);
    player_send(player, om);
    om_cleanup(om);
}
