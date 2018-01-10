#include "player.h"
#include "lib/dyad/dyad.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void player_init(player *p) {
    printf("Player initialise %s\n", dyad_getAddress(p->stream));
}

void player_cleanup(player *p) {
    printf("Player cleanup %s\n", dyad_getAddress(p->stream));
    free(p);
}