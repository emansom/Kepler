#include <stdio.h>

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
player *player_create(void *socket, char *ip_address) {
    player *p = malloc(sizeof(player));
    p->room_user = room_user_create();
    p->messenger = messenger_create();
    p->stream = socket;
    p->ip_address = strdup(ip_address);
    p->player_data = NULL;
    p->disconnected = 0;
    return p;
}

/**
 * Creates a new player data instance
 * @return player data struct
 */
player_data *player_create_data(int id, char *username, char *password, char *figure, int credits, char *motto, char *sex, int tickets, int film, int rank, char *console_motto, char *last_online) {
    player_data *data = malloc(sizeof(player_data));
    data->id = id;
    data->username = strdup(username);
    data->password = strdup(password);
    data->figure = strdup(figure);
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
void player_login(player *player) {
    query_player_save_last_online(player);
    room_manager_add_by_user_id(player->player_data->id);
    messenger_init(player);
}

/**
 * Send an outgoing message to the socket
 * 
 * @param p the player struct
 * @param om the outgoing message
 */
void player_send(player *p, outgoing_message *om) {
    if (om == NULL || p == NULL) {
        return;
    }

    om_finalise(om);
    char *data = om->sb->data;

    uv_handle_t *handle = p->stream;
    uv_write_t *req = (uv_write_t *) malloc(sizeof(uv_write_t));
    uv_buf_t wrbuf = uv_buf_init(data, strlen(data));

    int r = uv_write(req, (uv_stream_t *)handle, &wrbuf, 1, server_on_write);

    if (r) {
        //printf("Error sending message\n");
    } else {
        //printf("Client [%s]: %s\n", p->ip_address, data);
    }
}

/**
 * Sends the key of an error, whose description value is inside the external_texts of the client.
 * 
 * @param p the player
 * @param error the error message
 */
void send_localised_error(player *p, char *error) {
    outgoing_message *om = om_create(33); // @a
    om_write_str(om, error);
    player_send(p, om);
    om_cleanup(om);
}

/**
 * Send an alert to the player
 * 
 * @param p the player
 * @param greeting the alert message
 */
void send_alert(player *p, char *greeting) {
    outgoing_message *welcome_message = om_create(139); // BK
    om_write_str(welcome_message, greeting);
    player_send(p, welcome_message);
    om_cleanup(welcome_message);
}

/**
 * Called when a connection is closed
 * @param player the player struct
 */
void player_cleanup(player *player) {
    if (player == NULL) {
        return;
    }

    query_player_save_last_online(player);
    player_manager_remove(player);

    if (player->room_user->room != NULL) {
        room_leave(player->room_user->room, player);
    }

    if (player->player_data != NULL) {
        List *rooms = room_manager_get_by_user_id(player->player_data->id);

        for (int i = 0; i < list_size(rooms); i++) {
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
    }

    if (player->messenger != NULL) {
        messenger_cleanup(player->messenger);
    }

    if (player->player_data != NULL) {
        player_data_cleanup(player->player_data);
        player->player_data = NULL;
    }

    free(player->ip_address);
    free(player->stream);
    free(player);

    player = NULL;
}

void player_data_cleanup(player_data *player_data) {
    free(player_data->username);
    free(player_data->figure);
    free(player_data->motto);
    free(player_data->console_motto);
    free(player_data->sex);
    free(player_data);
}