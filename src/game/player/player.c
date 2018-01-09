#include "player.h"
#include "shared.h"

void player_init(player *p) {
    printf("Player initialise %s\n", dyad_getAddress(p->stream));
}

void player_cleanup(player *p) {
    printf("Player cleanup %s\n", dyad_getAddress(p->stream));
    free(p);
}