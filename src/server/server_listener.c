#include "lib/dyad/dyad.h"
#include "communication/messages/incoming_message.h"
#include "communication/message_handler.h"

dyad_Stream *streams[50];


/**
 * Handle incoming data from the socket
 * @param e the socket event
 */
static void handle_data(dyad_Event *e) {
    /*if (streams[0] != NULL) {
        if (streams[0] == e->stream) {
            printf("EQUALS!\n");
        }
    }*/

    if (strlen(e->data) < 4) {
        return;
    }

    int amount_read = 0;

    while (amount_read < strlen(e->data)) {
        char recv_length[] = {
                e->data[amount_read++],
                e->data[amount_read++],
                e->data[amount_read++],
                e->data[amount_read++]
        };

        int message_length = strtol(recv_length, NULL, 10) + 1;

        if (!message_length) {
            continue;
        }

        char *message = malloc(message_length * sizeof(char));

        for (int i = 0; i < message_length - 1; i++) {
            message[i] = e->data[amount_read++];
        }

        message[message_length - 1] = '\0';
        printf("Client [%s] incoming data (len: %i): %s\n", dyad_getAddress(e->stream), message_length - 1, message);

        incoming_message *im = im_create(message);
        mh_invoke_message(im, e->stream);

        im_cleanup(im);
        free(message);
    }
}

/**
 * Handle client disconnect
 * @param e the socket event
 */
static void client_disconnect(dyad_Event *e) {
    /*if (streams[0] != NULL) {
        if (streams[0] == e->stream) {
            printf("BYEEE!\n");
        }
    }*/
    printf ("Client [%s] has disconnected\n", dyad_getAddress(e->stream));
}

/**
 * Handle client connection
 * @param e the socket event
 */
static void client_connect(dyad_Event *e) {
    /*if (streams[0] == NULL) {
        streams[0] = e->remote;
    }*/
    printf("Client [%s] has connected\n", dyad_getAddress(e->remote));
    dyad_addListener(e->remote, DYAD_EVENT_DATA, handle_data, NULL);
    dyad_addListener(e->remote, DYAD_EVENT_CLOSE, client_disconnect, NULL);

    char *handshake = "###HELLO\r##";
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
    dyad_listenEx(server, "0.0.0.0", 37120, 512);

    while (dyad_getStreamCount() > 0) {
        dyad_update();
    }

    dyad_shutdown();
}