#include "player_manager.h"
#include "shared.h"

#include "lib/collections/list.h"
#include "lib/dyad/dyad.h"
#include "player.c"

void player_manager_init() {
    list_new(&global.player_manager.players);
}

player *player_manager_add(dyad_Stream *stream) {
    player *p = malloc(sizeof(player));
    p->stream = stream;

    player_init(p);
    list_add(global.player_manager.players, p);

    return p;
}

void player_manager_remove(dyad_Stream *stream) {
    ListIter iter;
    list_iter_init(&iter, global.player_manager.players);

    player *p;
    while (list_iter_next(&iter, (void*) &p) != CC_ITER_END) {
        if (p->stream == stream) {
            list_diter_remove(&iter, NULL);
            break;
        }
    }
}

player *player_manager_find(dyad_Stream *stream) {
    ListIter iter;
    list_iter_init(&iter, global.player_manager.players);

    player *p;
    while (list_iter_next(&iter, (void*) &p) != CC_ITER_END) {
        if (p->stream == stream) {
            return p;
        }
    }
}