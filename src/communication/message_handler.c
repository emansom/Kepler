#include "message_handler.h"
#include "game/player/player.h"

#include "communication/incoming/login/InitCryptoMessageEvent.h"
#include "communication/incoming/login/SecretKeyMessageEvent.h"
#include "communication/incoming/login/LoginMessageEvent.h"
#include "communication/incoming/login/UserInfoMessageEvent.h"

#include "communication/incoming/navigator/NavigatorMessageEvent.h";

/**
 * Assigns all header handlers to this array
 */
void mh_add_messages() {
    message_requests[206] = message_initcrypto;
    message_requests[202] = message_secretkey;
    message_requests[4] = message_login;
    message_requests[7] = message_userinfo;
    message_requests[150] = message_navigator;
}

/**
 * Retrieves the handler by header id
 * @param im the incoming message struct
 * @param player the player struct
 */
void mh_invoke_message(incoming_message *im, player *player) {
    if (message_requests[im->header_id] == NULL) {
        return;
    }

    mh_request handle = message_requests[im->header_id];
    handle(player, im);
}