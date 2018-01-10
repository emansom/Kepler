
#include <stdio.h>

#include "player.h"
#include "lib/dyad/dyad.h"

#include "util/stringbuilder.h"
#include "communication/messages/outgoing_message.h"

void player_init(player *p) {
    printf("Player initialise %s\n", dyad_getAddress(p->stream));
}

void player_cleanup(player *p) {
    printf("Player cleanup %s\n", dyad_getAddress(p->stream));
    free(p);
}

void player_send(player *p, outgoing_message *om) {
    om_finalise(om);
    dyad_write(p->stream, om->sb->data, strlen(om->sb->data));
    om_cleanup(om);
}