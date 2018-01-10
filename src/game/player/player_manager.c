#include "shared.h"

#include "lib/collections/list.h"
#include "lib/dyad/dyad.h"

#include "player.h"

/**
 * Create a new list to store players
 */
void player_manager_init() {
    list_new(&global.player_manager.players);
}

/**
 * Creates a new player when given a new network stream to add. Will return a
 * old player if the stream was already added.
 *
 * @param stream the dyad stream
 * @return the player
 */
player *player_manager_add(dyad_Stream *stream) {
    player *existing = player_manager_find(stream);

    if (existing != NULL) {
        return existing;
    }

    player *p = player_create(stream);

    player_init(p);
    list_add(global.player_manager.players, p);

    return p;
}

/**
 * Removes a player by the given stream
 * @param stream the dyad stream
 */
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

/**
 * Finds a player by a given dyad stream
 * @param stream the dyad stream
 * @return the player
 */
player *player_manager_find(dyad_Stream *stream) {
    ListIter iter;
    list_iter_init(&iter, global.player_manager.players);

    player *p;
    while (list_iter_next(&iter, (void*) &p) != CC_ITER_END) {
        if (p->stream == stream) {
            return p;
        }
    }

    return NULL;
}