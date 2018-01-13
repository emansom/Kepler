#include <stdio.h>

#include "hashtable.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "communication/message_handler.h"
#include "util/encoding/base64encoding.h"

#include "server/server_listener.h"

uv_loop_t *loop;

void server_alloc_buffer(uv_handle_t* handle, size_t  size, uv_buf_t* buf) {
    buf->base = malloc(size);
    buf->len = size;
}

void server_on_connection_close(uv_handle_t *handle) {
    printf("[libhh] disposed a connection\n");
}

void server_on_write(uv_write_t* req, int status) {
    if (status) {
        fprintf(stderr, "uv_write error: %s\n", uv_strerror(status));
        return;
    }
 }

void server_on_read(uv_stream_t *handle, ssize_t nread, const uv_buf_t *buf) {
    if(nread == UV_EOF) {
        uv_close((uv_handle_t *) handle, server_on_connection_close);
        return;
    }

    if(nread > 0) {\
        player *p = handle->data;
        int amount_read = 0;

        while (amount_read < nread) {
            char recv_length[] = {
                    buf->base[amount_read++],
                    buf->base[amount_read++],
                    buf->base[amount_read++],
                    '\0'
            };

            int message_length = base64_decode(recv_length) + 1;

            if (message_length < 0 || message_length > 5120) {
                continue;
            }

            char *message = malloc(message_length * sizeof(char));

            for (int i = 0; i < message_length - 1; i++) {
                message[i] = buf->base[amount_read++];
            }

            message[message_length - 1] = '\0';

            if (p != NULL) {
                incoming_message *im = im_create(message);
                mh_invoke_message(im, p);
                im_cleanup(im);
            }

            free(message);
        }
    } else {
        uv_close((uv_handle_t *) handle, server_on_connection_close);
    }

    free(buf->base);
}

void server_on_new_connection(uv_stream_t *server, int status) {
    uv_tcp_t *client = malloc(sizeof(uv_tcp_t));
    uv_tcp_init(uv_default_loop(), client);

    struct sockaddr_in client_addr;    
    int client_addr_length;

    uv_stream_t *handle = (uv_stream_t*)client;
    uv_tcp_getpeername((const uv_tcp_t*) handle, (struct sockaddr*)&client_addr, &client_addr_length);
    char *ip = inet_ntoa(client_addr.sin_addr);

    player *p = player_manager_add(handle);
    client->data = p;

    int result = uv_accept(server, handle);

    if(result == 0) {
        char *handshake = "@@\1";
        
        uv_write_t *req = (uv_write_t *) malloc(sizeof(uv_write_t));
        uv_buf_t wrbuf = uv_buf_init(handshake, strlen(handshake));
        uv_write(req, handle, &wrbuf, 1, server_on_write);

        uv_read_start(handle, server_alloc_buffer, server_on_read);
    } else {
        uv_close((uv_handle_t *) handle, server_on_connection_close);
    }
}

void start_server(const char *ip, int port) {
    uv_loop_t *loop = uv_default_loop();

    uv_tcp_t server;
    struct sockaddr_in bind_addr;

    uv_tcp_init(loop, &server);
    uv_ip4_addr(ip, port, &bind_addr);
    uv_tcp_bind(&server, (const struct sockaddr *) &bind_addr, 0);
    uv_listen((uv_stream_t *) &server, 128, server_on_new_connection);

    // At the moment, the entire server is single threaded but there are plans
    // to make the entire server multithreaded.

    uv_run(loop, UV_RUN_DEFAULT);
    uv_loop_close(loop);
}