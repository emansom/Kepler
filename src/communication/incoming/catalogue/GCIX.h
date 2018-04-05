#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "list.h"

void GETUSERFLATCATS(player *player, incoming_message *message) {
    outgoing_message *navigator = om_create(221); // "C]"

    player_send(player, navigator);
    om_cleanup(navigator);
    
    list_destroy(categories);
}
