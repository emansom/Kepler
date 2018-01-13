#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_STAT(player *player, incoming_message *message) {
    char var[11];
    sprintf(var, "%i", player->player_data->id);
    var[10] = '\0';

    outgoing_message *om = om_create(34); // "@b"
    sb_add_string(om->sb, "0");
    sb_add_string(om->sb, " ");
    sb_add_string(om->sb, "3");
    sb_add_string(om->sb, ",");
    sb_add_string(om->sb, "5");
    sb_add_string(om->sb, ",");
    sb_add_string(om->sb, "0.0");
    sb_add_string(om->sb, ",");
    sb_add_string(om->sb, "2");
    sb_add_string(om->sb, ",");
    sb_add_string(om->sb, "2");
    sb_add_string(om->sb, "/");
    sb_add_char(om->sb, 13);
    player_send(player, om);
    om_cleanup(om);
}
