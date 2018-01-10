#include "message_handler.h"
#include "game/player/player.h"

#include "communication/incoming/InitCryptoMessageEvent.h"
#include "communication/incoming/SecretKeyMessageEvent.h"
#include "communication/incoming/LoginMessageEvent.h"

void mh_add_messages() {
    message_requests[206] = message_initcrypto;
    message_requests[202] = message_secretkey;
    message_requests[4] = message_login;
}

void mh_invoke_message(incoming_message *im, player *player) {
    if (message_requests[im->header_id] == NULL) {
        return;
    }

    mh_request handle = message_requests[im->header_id];
    handle(player, im);
}