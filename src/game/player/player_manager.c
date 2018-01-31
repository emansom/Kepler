#include "shared.h"

#include "list.h"
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
player *player_manager_add(void *stream, char *ip) {
    player *p = player_create(stream, ip);
    list_add(global.player_manager.players, p);
    return p;
}

/**
 * Removes a player by the given stream
 * @param stream the dyad stream
 */
void player_manager_remove(player *p) {
    if (list_contains(global.player_manager.players, p)) {
        printf("lol\n");
        list_remove(global.player_manager.players, p, NULL);
    }
}

/**
 * Finds a player by a given dyad stream
 * @param stream the dyad stream
 * @return the player
 */
player *player_manager_find(void *stream) {
    return NULL;
}

player *player_manager_find_by_id(int player_id) {
	for (int i = 0; i < list_size(global.player_manager.players); i++) {
		player *p;
		list_get_at(global.player_manager.players, i, (void*)&p);

        if (p->player_data->id == player_id) {
            return p;
        }
    }  

    return NULL;
}