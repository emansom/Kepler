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
session *player_manager_add(void *stream, char *ip) {
    session *p = player_create(stream, ip);
    list_add(global.player_manager.players, p);
    return p;
}

/**
 * Removes a player by the given stream
 * @param stream the dyad stream
 */
void player_manager_remove(session *p) {
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
session *player_manager_find(void *stream) {
    return NULL;
}

/**
 * Find a player by user id
 *
 * @param player_id the player id
 * @return the player, if sound, otherwise returns NULL
 */
session *player_manager_find_by_id(int player_id) {
    if (list_size(global.player_manager.players) > 0) {
        for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
            session *p;
            list_get_at(global.player_manager.players, i, (void *) &p);

            if (!p->logged_in) {
                continue;
            }

            if (p->player_data->id == player_id) {
                return p;
            }
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
session *player_manager_find_by_name(char *name) {
    if (list_size(global.player_manager.players) > 0) {
        for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
            session *p;
            list_get_at(global.player_manager.players, i, (void *) &p);

            if (!p->logged_in) {
                continue;
            }

            if (strcmp(p->player_data->username, name) == 0) {
                return p;
            }
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
    if (list_size(global.player_manager.players) > 0) {
        for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
            session *p;
            list_get_at(global.player_manager.players, i, (void *) &p);

            if (p->player_data->id == player_id) {
                return (player_data *) p->player_data;
            }
        }
    }

    return player_query_data(player_id);
}

/**
* Destroy session by player id
*
* @param player_id the player id
*/
void player_manager_destroy_session_by_id(int player_id) {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        session *p;
        list_get_at(global.player_manager.players, i, (void*)&p);

        if (!p->logged_in || p->player_data->id != player_id) {
            continue;
        }

        uv_close((uv_handle_t *) p->stream, server_on_connection_close);
    }
}

/**
 * Dispose model manager
 */
void player_manager_dispose() {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        session *player;
        list_get_at(global.player_manager.players, i, (void *) &player);

        uv_close((uv_handle_t *) player->stream, server_on_connection_close);
    }

    list_destroy(global.player_manager.players);
}