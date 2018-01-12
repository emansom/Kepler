#include "shared.h"

#include "list.h"
#include "hashtable.h"

#include "dyad.h"
#include "player.h"

/**
 * Create a new list to store players
 */
void player_manager_init() {
    hashtable_new(&global.player_manager.players);
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

    if (existing == NULL) {
        player *p = player_create(stream);
        stream->player = p;

        player_init(p);
        hashtable_add(global.player_manager.players, stream, p);
    }

    return stream->player;
}

/**
 * Removes a player by the given stream
 * @param stream the dyad stream
 */
void player_manager_remove(dyad_Stream *stream) {
    if (hashtable_contains_key(global.player_manager.players, stream)) {
        hashtable_remove(global.player_manager.players, stream, NULL);
    }
}

/**
 * Finds a player by a given dyad stream
 * @param stream the dyad stream
 * @return the player
 */
player *player_manager_find(dyad_Stream *stream) {
    void *out = NULL;

    if (hashtable_contains_key(global.player_manager.players, stream)) {
        hashtable_get(global.player_manager.players, stream, &out);
    }

    if (out != NULL) {
        player *player = out;
        return player;
    }

    return out;
}