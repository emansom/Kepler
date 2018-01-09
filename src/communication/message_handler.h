#ifndef MESSAGE_HANDLER_H
#define MESSAGE_HANDLER_H

#include <shared.h>
#include "communication/messages/incoming_message.h"

#define MESSAGES 50
typedef struct dyad_Stream dyad_Stream;

static int messages_registered = 0;
typedef void (*mh_request)(request);

typedef struct {
    mh_request request_handler;
    char *header;
} message_handle;


message_handle message_requests[MESSAGES];

void mh_invoke_message(incoming_message *, dyad_Stream *pStream);
void mh_add_messages();

message_handle mh_create_handle(mh_request, char*);

#endif