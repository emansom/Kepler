#ifndef INCOMING_MESSAGE_H
#define INCOMING_MESSAGE_H

#include <shared.h>
#include "lib/dyad/dyad.h"

typedef struct {
    char *header;
    char *data;
} incoming_message;

typedef struct {
    incoming_message *im;
    dyad_Stream *stream;
} request;

incoming_message *im_create(char*);
char *im_get_argument(incoming_message*, const char*, int);
void im_cleanup(incoming_message*);

#endif