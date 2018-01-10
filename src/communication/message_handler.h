#ifndef MESSAGE_HANDLER_H
#define MESSAGE_HANDLER_H

#include <shared.h>
#include "communication/messages/incoming_message.h"

#define MESSAGES 9999
typedef struct dyad_Stream dyad_Stream;
typedef struct player_s player;

typedef void (*mh_request)(player*, incoming_message*);

mh_request message_requests[MESSAGES];

void mh_invoke_message(incoming_message*, player*);
void mh_add_messages();

#endif