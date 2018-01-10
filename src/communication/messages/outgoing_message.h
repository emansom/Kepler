#ifndef INCOMING_MESSAGE_H
#define INCOMING_MESSAGE_H

#include <shared.h>

typedef struct player_s player;
typedef struct stringbuilder_s stringbuilder;
typedef struct dyad_Stream dyad_Stream;

typedef struct {
    stringbuilder *sb;
} outgoing_message;

outgoing_message *om_create(int);
void om_write_str(outgoing_message*, char*);
void om_cleanup(outgoing_message*);

#endif