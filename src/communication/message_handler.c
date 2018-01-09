#include "message_handler.h"
#include "game/player/player.h"
#include "communication/incoming/VERSIONCHECK.h"

void mh_add_messages() {
    message_requests[messages_registered++] = mh_create_handle(message_versioncheck, "VERSIONCHECK");
}

void mh_invoke_message(incoming_message *im, player *player) {
    request req;
    req.im = im;
    req.player = player;
    req.stream = player->stream;

    for (int i = 0; i < messages_registered; i++) {
        message_handle handle = message_requests[i];

        if (strcmp(handle.header, im->header) == 0) {
            handle.request_handler(req);
            break;
        }
    }
}

message_handle mh_create_handle(mh_request request, char *header) {
    message_handle handler;
    handler.request_handler = request;
    handler.header = header;
    return handler;
}