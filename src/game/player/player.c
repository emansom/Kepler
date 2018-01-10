
#include <stdio.h>

#include "player.h"
#include "lib/dyad/dyad.h"

#include "util/stringbuilder.h"
#include "communication/messages/outgoing_message.h"

/**
 * Called when a connection is successfully opened
 * @param p the player struct
 */
void player_init(player *p) {
    printf("Player initialise %s\n", dyad_getAddress(p->stream));
}

/**
 * Called when a connection is closed
 * @param p the player struct
 */
void player_cleanup(player *p) {
    printf("Player cleanup %s\n", dyad_getAddress(p->stream));
    free(p);
}

/**
 * Send an outgoing message to the socket
 * @param p the player struct
 * @param om the outgoing message
 */
void player_send(player *p, outgoing_message *om) {
    om_finalise(om);

    char *data = om->sb->data;
    dyad_write(p->stream, data, strlen(data));

    printf ("Client [%s] outgoing data: %i / %s\n", dyad_getAddress(p->stream), om->header_id, data);
    om_cleanup(om);
}