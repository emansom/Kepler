#ifndef MESSENGER_GETMESSAGES_H
#define MESSENGER_GETMESSAGES_H

#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void MESSENGER_GETMESSAGES(player *p, incoming_message *message) {
    for (int i = 0; i < list_size(p->messenger->messages); i++) {
        messenger_message *message;
        list_get_at(p->messenger->messages, i, (void*)&message);

        outgoing_message *response = om_create(132); // "BD"
        om_write_int(response, 1);
        om_write_int(response, message->id);
        om_write_int(response, message->sender_id);
        om_write_str(response, "dd-MM-yyyy HH:mm:ss");
        om_write_str(response, message->body);
        player_send(p, response);
        om_cleanup(response);
    }
}

#endif