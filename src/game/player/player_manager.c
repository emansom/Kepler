#include "shared.h"

#include "list.h"
#include "player.h"

#include "database/queries/player_query.h"
#include "server/server_listener.h"
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
        list_remove(global.player_manager.players, p, NULL);
    }
}

/**
 * Finds a player by a given stream
 * 
 * @param stream the stream
 * @return the player
 */
player *player_manager_find(void *stream) {
    return NULL;
}

/**
 * Find a player by user id
 * 
 * @param player_id the player id
 * @return the player, if sound, otherwise returns NULL
 */
player *player_manager_find_by_id(int player_id) {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        player *p;
        list_get_at(global.player_manager.players, i, (void*)&p);

        if (p->player_data->id == player_id) {
            return p;
        }
    }  

    return NULL;
}

/**
 * Find a player by user id
 *
 * @param player_id the player id
 * @return the player, if sound, otherwise returns NULL
 */
player *player_manager_find_by_name(char *name) {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        player *p;
        list_get_at(global.player_manager.players, i, (void*)&p);

        if (strcmp(p->player_data->username, name) == 0) {
            return p;
        }
    }

    return NULL;
}

/**
 * Find a player by user id
 * 
 * @param player_id the player id
 * @return the player, if sound, otherwise returns NULL
 */
player_data *player_manager_get_data_by_id(int player_id) {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        player *p;
        list_get_at(global.player_manager.players, i, (void*)&p);

        if (p->player_data->id == player_id) {
            return (player_data *) p->player_data;
        }
    }  

    return player_query_data(player_id);
}

/**
 * Dispose model manager
 */
void player_manager_dispose() {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        player *player;
        list_get_at(global.player_manager.players, i, (void *) &player);

        uv_close((uv_handle_t *) player->stream, server_on_connection_close);
        player_cleanup(player);
    }

    list_destroy(global.player_manager.players);
}