#include "list.h"

#include "rcon_handler.h"
#include "rcon_listener.h"

#include "game/player/player_manager.h"
#include "game/player/player.h"

#include "shared.h"
#include "log.h"

void rcon_handle_command(uv_stream_t *handle, int header, char *message) {
    if (header == 1) { // "GET_USERS"
        char users_online[10];
        sprintf(users_online, "%i", (int) list_size(global.player_manager.players));

        rcon_send(handle, users_online);
    }

    if (header == 2) { // "REFRESH_APPEARANCE"
        int player_id = (int)strtol(message, NULL, 10);

        log_debug("RCON: refresh appearance for user id %u", player_id);

        session *p = player_manager_find_by_id(player_id);

        if (p == NULL) {
            return;
        }

        player_refresh_appearance(p);
    }
}

void rcon_send(uv_stream_t *handle, char *data) {
    uv_write_t *req;

    if(!(req = malloc(sizeof(uv_write_t)))){
        return;
    }

    size_t message_length = strlen(data);

    uv_buf_t buffer = uv_buf_init(malloc(message_length), (unsigned int) message_length);
    memcpy(buffer.base, data, message_length);

    req->handle = handle;
    req->data = buffer.base;

    int response = uv_write(req, handle, &buffer, 1, &rcon_on_write);
}
