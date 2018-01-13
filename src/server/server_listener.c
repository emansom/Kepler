#include "dyad.h"

#include "hashtable.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "communication/message_handler.h"
#include "util/encoding/base64encoding.h"

/**
 * Handle incoming data from the socket
 * @param e the socket event
 */
static void handle_data(dyad_Event *e) {
    if (strlen(e->data) < 5) {
        return;
    }

    int amount_read = 0;

    while (amount_read < strlen(e->data)) {
        char recv_length[] = {
                e->data[amount_read++],
                e->data[amount_read++],
                e->data[amount_read++],
                '\0'
        };

        int message_length = base64_decode(recv_length) + 1;

        if (message_length < 0 || message_length > 5120) {
            return;
        }

        char *message = malloc(message_length * sizeof(char));

        for (int i = 0; i < message_length - 1; i++) {
            message[i] = e->data[amount_read++];
        }

        message[message_length - 1] = '\0';

        player *player = player_manager_find(e->stream);

        if (player != NULL) {
            incoming_message *im = im_create(message);
            mh_invoke_message(im, player);
            im_cleanup(im);
        }

        free(message);
    }
}

/**
 * Handle client disconnect
 * @param e the socket event
 */
static void client_disconnect(dyad_Event *e) {
    player *player = player_manager_find(e->stream);

    if (player != NULL) {
        player_manager_remove(e->stream);
        player_cleanup(player);
    }
}

/**
 * Handle client connection
 * @param e the socket event
 */
static void client_connect(dyad_Event *e) {
    player_manager_add(e->remote);
    printf("Client [%s] has connected\n", dyad_getAddress(e->remote));

    dyad_addListener(e->remote, DYAD_EVENT_DATA, handle_data, NULL);
    dyad_addListener(e->remote, DYAD_EVENT_CLOSE, client_disconnect, NULL);

    printf("size players: %i\n", hashtable_size(global.player_manager.players));

    char *handshake = "@@\1";
    dyad_write(e->remote, handshake, strlen(handshake));
}

/**
 * Create the server instance
 */
dyad_Stream *create_server() {
    dyad_init();

    dyad_Stream *server = dyad_newStream();
    dyad_addListener(server, DYAD_EVENT_ACCEPT, client_connect, NULL);

    return server;
}

/**
 * Listen on the server
 */
void listen_server(dyad_Stream *server) {
    dyad_listenEx(server, "0.0.0.0", 12321, 512);

    while (dyad_getStreamCount() > 0) {
        dyad_update();
    }

    dyad_shutdown();
}