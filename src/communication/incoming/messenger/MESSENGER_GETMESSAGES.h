#ifndef MESSENGER_GETMESSAGES_H
#define MESSENGER_GETMESSAGES_H

#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void MESSENGER_GETMESSAGES(session *p, incoming_message *message) {
    for (int i = 0; i < list_size(p->messenger->messages); i++) {
        messenger_message *message;
        list_get_at(p->messenger->messages, i, (void*)&message);

        outgoing_message *response = om_create(134); // "BF"
        om_write_int(response, 1);
        om_write_int(response, message->id);
        om_write_int(response, message->receiver_id);
        om_write_str(response, message->date);
        om_write_str(response, message->body);
        session_send(p, response);
        om_cleanup(response);
    }
}

#endif