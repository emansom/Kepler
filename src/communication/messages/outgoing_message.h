#ifndef OUTGOING_MESSAGE_H
#define OUTGOING_MESSAGE_H

#include <shared.h>

typedef struct player_s player;
typedef struct stringbuilder_s stringbuilder;
typedef struct dyad_Stream dyad_Stream;

typedef struct outgoing_message_s {
    int header_id;
    char *header;
    int finalised;
    stringbuilder *sb;
} outgoing_message;

outgoing_message *om_create(int);
void om_write_str(outgoing_message*, char*);
void om_write_str_int(outgoing_message*, int);
void om_write_int(outgoing_message*, int);
void om_finalise(outgoing_message*);
void om_cleanup(outgoing_message*);

#endif