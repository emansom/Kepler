#ifndef MESSAGE_HANDLER_H
#define MESSAGE_HANDLER_H

#define MESSAGES 9999

typedef struct incoming_message_s incoming_message;
typedef struct session_s session;

typedef void (*mh_request)(session*, incoming_message*);
mh_request message_requests[MESSAGES];

void message_handler_invoke(incoming_message *, session *);
void message_handler_init();

#endif