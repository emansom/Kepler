#include <game/room/room_model.h>
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_STAT(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char var[11];
    sprintf(var, "%i", player->player_data->id);
    var[10] = '\0';

    outgoing_message *om = om_create(34); // "@b"
    sb_add_string(om->sb, "0");
    sb_add_string(om->sb, " ");
    sb_add_int(om->sb, player->room_user->room->room_data->model_data->door_x);
    sb_add_string(om->sb, ",");
    sb_add_int(om->sb, player->room_user->room->room_data->model_data->door_y);
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
