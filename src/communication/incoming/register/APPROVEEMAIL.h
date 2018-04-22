#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void APPROVEEMAIL(session *player, incoming_message *message) {
    outgoing_message *om = om_create(271); // "DO"
    session_send(player, om);
    om_cleanup(om);
}
