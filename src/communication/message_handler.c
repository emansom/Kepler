#include <communication/messages/incoming_message.h>
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

// Room
#include "communication/incoming/room/GETINTERST.h"
#include "communication/incoming/room/GETFLATINFO.h"
#include "communication/incoming/room/room_directory.h"
#include "communication/incoming/room/TRYFLAT.h"
#include "communication/incoming/room/GOTOFLAT.h"
#include "communication/incoming/room/GETROOMAD.h"
#include "communication/incoming/room/G_HMAP.h"
#include "communication/incoming/room/G_OBJS.h"
#include "communication/incoming/room/G_ITEMS.h"
#include "communication/incoming/room/G_STAT.h"
#include "communication/incoming/room/G_USRS.h"
#include "communication/incoming/room/GET_FURNI_REVISIONS.h"

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

    /*Client [127.0.0.1] incoming data: 21 / @U1
Client [127.0.0.1] incoming data: 182 / Bvgeneral
Client [127.0.0.1] incoming data: 182 / Bvgeneral
Client [127.0.0.1] incoming data: 88 / AXCarryItem
Client [127.0.0.1] incoming data: 2 / @B@IH
Client [127.0.0.1] incoming data: 57 / @y1
Client [127.0.0.1] incoming data: 59 / @{1
Client [127.0.0.1] incoming data: 88 / AXCarryItem
Client [127.0.0.1] incoming data: 126 / A~
Client [127.0.0.1] incoming data: 60 / @|
Client [127.0.0.1] incoming data: 61 / @}
Client [127.0.0.1] incoming data: 62 / @~
Client [127.0.0.1] incoming data: 63 / @
Client [127.0.0.1] incoming data: 213 / CU*/

    // Room
    message_requests[21] = GETFLATINFO;
    message_requests[182] = GETINTERST;
    message_requests[2] = room_directory;
    message_requests[57] = TRYFLAT;
    message_requests[59] = GOTOFLAT;
    message_requests[126] = GETROOMAD;
    message_requests[60] = G_HMAP;
    message_requests[62] = G_OBJS;
    message_requests[64] = G_STAT;
    message_requests[63] = G_ITEMS;
    message_requests[61] = G_USRS;
    message_requests[213] = GET_FURNI_REVISIONS;
}

/**
 * Retrieves the handler by header id
 * @param im the incoming message struct
 * @param player the player struct
 */
void mh_invoke_message(incoming_message *im, player *player) {
    printf("Client [%s] incoming data: %i / %s\n", dyad_getAddress(player->stream), im->header_id, im->data);

    if (message_requests[im->header_id] == NULL) {
        return;
    }

    mh_request handle = message_requests[im->header_id];
    handle(player, im);
}