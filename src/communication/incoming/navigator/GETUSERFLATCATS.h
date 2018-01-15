#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void GETUSERFLATCATS(player *player, incoming_message *message) {
    outgoing_message *navigator = om_create(221); // "C]"
    om_write_int(navigator, 1); // category count
    om_write_int(navigator, 1); // category id
    om_write_str(navigator, "Test"); // category name

    player_send(player, navigator);
    om_cleanup(navigator);

}
