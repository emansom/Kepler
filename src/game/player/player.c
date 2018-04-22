#include <stdio.h>

#include "game/inventory/inventory.h"
#include "game/messenger/messenger.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_manager.h"

#include "util/stringbuilder.h"
#include "communication/messages/outgoing_message.h"

#include "server/server_listener.h"
#include "database/queries/player_query.h"

#include "uv.h"
#include "list.h"

/**
 * Creates a new player
 * @return player struct
 */
session *player_create(void *socket, char *ip_address) {
    session *p = malloc(sizeof(session));
    p->stream = socket;
    p->disconnected = false;
    p->ip_address = strdup(ip_address);
    p->player_data = NULL;
    p->logged_in = false;
    p->room_user = NULL;
    p->messenger = NULL;
    p->inventory = NULL;
    return p;
}

/**
 * Creates a new player data instance
 * @return player data struct
 */
player_data *player_create_data(int id, char *username, char *password, char *figure, char *pool_figure, int credits, char *motto, char *sex, int tickets, int film, int rank, char *console_motto, char *last_online) {
    player_data *data = malloc(sizeof(player_data));
    data->id = id;
    data->username = strdup(username);
    data->password = strdup(password);
    data->figure = strdup(figure);
    data->pool_figure = strdup(pool_figure);
    data->credits = credits;
    data->motto = strdup(motto);
    data->console_motto = strdup(console_motto);
    data->sex = strdup(sex);
    data->tickets = tickets;
    data->film = film;
    data->rank = rank;
    data->last_online = strtoul(last_online, NULL, 10);
    return data;
}

/**
 * Called when a connection is successfully opened
 * 
 * @param p the player struct
 */
void player_login(session *player) {
    player->room_user = (void*)room_user_create(player);

    player->messenger = (void*)messenger_create();
    messenger_init(player);

    player->inventory = (void*)inventory_create();
    inventory_init(player);

    player_query_save_last_online(player);
    room_manager_add_by_user_id(player->player_data->id);

    player->logged_in = true;
}

/**
 * Send an outgoing message to the socket
 * 
 * @param p the player struct
 * @param om the outgoing message
 */
void session_send(session *p, outgoing_message *om) {
    if (om == NULL || p == NULL || p->disconnected) {
        return;
    }

    om_finalise(om);

    uv_write_t *req;

    if(!(req = malloc(sizeof(uv_write_t)))){
        return;
    }

    size_t message_length = strlen(om->sb->data);

    uv_buf_t buffer = uv_buf_init(malloc(message_length), (unsigned int) message_length);
    memcpy(buffer.base, om->sb->data, message_length);

    req->handle = (void*) p;
    req->data = buffer.base;

    int response = uv_write(req, (uv_stream_t *) p->stream, &buffer, 1, &server_on_write);
}

/**
 * Sends the key of an error, whose description value is inside the external_texts of the client.
 * 
 * @param p the player
 * @param error the error message
 */
void send_localised_error(session *p, char *error) {
    outgoing_message *om = om_create(33); // @a
    om_write_str(om, error);
    session_send(p, om);
    om_cleanup(om);
}

/**
 * Send an alert to the player
 * 
 * @param p the player
 * @param greeting the alert message
 */
void send_alert(session *p, char *greeting) {
    outgoing_message *welcome_message = om_create(139); // BK
    om_write_str(welcome_message, greeting);
    session_send(p, welcome_message);
    om_cleanup(welcome_message);
}

/**
 * Send credit amount to player.
 *
 * @param player the player to send to
 */
void session_send_credits(session *player) {
    char credits_string[10 + 1]; ///"num + /0";
    sprintf(credits_string, "%i", player->player_data->credits);

    outgoing_message *credits = om_create(6); // "@F"
    om_write_str(credits, credits_string);
    sb_add_string(credits->sb, ".0");
    session_send(player, credits);
    om_cleanup(credits);
}

/**
 * Send ticket amount to player.
 *
 * @param player the player to send to
 */
void session_send_tickets(session *player) {
    char credits_string[10 + 1]; ///"num + /0";
    sprintf(credits_string, "%i", player->player_data->tickets);

    outgoing_message *credits = om_create(124); // "A|"
    sb_add_string(credits->sb, credits_string);
    session_send(player, credits);
    om_cleanup(credits);
}

/**
 * Called when a connection is closed
 * @param player the player struct
 */
void player_cleanup(session *player) {
    if (player == NULL) {
        return;
    }

    player_manager_remove(player);

    if (player->room_user != NULL) {
        if (player->room_user->room != NULL) {
            room_leave(player->room_user->room, player);
        }
    }

    if (player->player_data != NULL) {
        player_query_save_last_online(player);

        List *rooms = room_manager_get_by_user_id(player->player_data->id);

        for (size_t i = 0; i < list_size(rooms); i++) {
            room *room;
            list_get_at(rooms, i, (void*)&room);

            if (room != NULL) {
                room_dispose(room);
            }
        }

        list_destroy(rooms);
    }

    if (player->room_user != NULL) {
        room_user_cleanup(player->room_user);
        player->room_user = NULL;
    }

    if (player->messenger != NULL) {
        messenger_dispose(player->messenger);
        player->messenger = NULL;
    }

    if (player->inventory != NULL) {
        inventory_dispose(player->inventory);
        player->inventory = NULL;
    }

    if (player->player_data != NULL) {
        player_data_cleanup(player->player_data);
        player->player_data = NULL;
    }

    free(player->ip_address);
}

void player_data_cleanup(player_data *player_data) {
    free(player_data->username);
    free(player_data->password);
    free(player_data->figure);
    free(player_data->pool_figure);
    free(player_data->motto);
    free(player_data->console_motto);
    free(player_data->sex);
    free(player_data);
}