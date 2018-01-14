#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_ITEMS(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    room_enter(player->room_user->room, player);

    outgoing_message *om = om_create(45); // "@m"
    player_send(player, om);
    om_cleanup(om);

    om = om_create(24); // "@X"
    om_write_int(om, 1);
    om_write_str(om, "do u kno de whey");
    player_send(player, om);
    om_cleanup(om);

    om = om_create(24); // "@X"
    om_write_int(om, 2);
    om_write_str(om, "do u kno de whey");
    player_send(player, om);
    om_cleanup(om);
}
