#include "message_handler.h"
#include "game/player/player.h"

// Login
#include "communication/incoming/login/INIT_CRYPTO.h"
#include "communication/incoming/login/GENERATEKEY.h"
#include "communication/incoming/login/TRY_LOGIN.h"

// User
#include "communication/incoming/user/GET_INFO.h"
#include "communication/incoming/user/GET_CREDITS.h"

// Navigator
#include "communication/incoming/navigator/NAVIGATE.h"
#include "communication/incoming/navigator/SUSERF.h"

/**
 * Assigns all header handlers to this array
 */
void mh_add_messages() {
    // Login
    message_requests[206] = INIT_CRYPTO;
    message_requests[202] = GENERATEKEY;
    message_requests[4] = TRY_LOGIN;

    // User
    message_requests[7] = GET_INFO;
    message_requests[8] = GET_CREDITS;

    // Navigator
    message_requests[150] = NAVIGATE;
    message_requests[16] = SUSERF;
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