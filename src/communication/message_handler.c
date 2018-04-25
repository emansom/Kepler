#include "communication/messages/incoming_message.h"

#include "message_handler.h"
#include <shared.h>

#include "game/player/player.h"

// Login
#include "communication/incoming/login/INIT_CRYPTO.h"
#include "communication/incoming/login/GENERATEKEY.h"
#include "communication/incoming/login/TRY_LOGIN.h"
#include "communication/incoming/login/GDATE.h"

// Register
#include "communication/incoming/register/APPROVENAME.h"
#include "communication/incoming/register/APPROVE_PASSWORD.h"
#include "communication/incoming/register/APPROVEEMAIL.h"
#include "communication/incoming/register/PARENT_EMAIL_REQUIRED.h"
#include "communication/incoming/register/CHECK_AGE.h"
#include "communication/incoming/register/REGISTER.h"

// User
#include "communication/incoming/user/GET_INFO.h"
#include "communication/incoming/user/GET_CREDITS.h"
#include "communication/incoming/user/UPDATE.h"
#include "communication/incoming/user/UPDATE_ACCOUNT.h"

// Messenger
#include "communication/incoming/messenger/MESSENGERINIT.h"
#include "communication/incoming/messenger/FINDUSER.h"
#include "communication/incoming/messenger/MESSENGER_ASSIGNPERSMSG.h"
#include "communication/incoming/messenger/MESSENGER_REQUESTBUDDY.h"
#include "communication/incoming/messenger/MESSENGER_DECLINEBUDDY.h"
#include "communication/incoming/messenger/MESSENGER_ACCEPTBUDDY.h"
#include "communication/incoming/messenger/MESSENGER_REMOVEBUDDY.h"
#include "communication/incoming/messenger/MESSENGER_GETREQUESTS.h"
#include "communication/incoming/messenger/MESSENGER_SENDMSG.h"
#include "communication/incoming/messenger/MESSENGER_GETMESSAGES.h"
#include "communication/incoming/messenger/MESSENGER_MARKREAD.h"

// Navigator
#include "communication/incoming/navigator/NAVIGATE.h"
#include "communication/incoming/navigator/SUSERF.h"
#include "communication/incoming/navigator/GETUSERFLATCATS.h"
#include "communication/incoming/navigator/RECOMMENDED_ROOMS.h"

// Room
#include "communication/incoming/room/GETINTERST.h"
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
#include "communication/incoming/room/LETUSERIN.h"

// Pool
#include "communication/incoming/room/pool/SWIMSUIT.h"
#include "communication/incoming/room/pool/SPLASHPOSITION.h"
#include "communication/incoming/room/pool/DIVE.h"
#include "communication/incoming/room/pool/SIGN.h"
#include "communication/incoming/room/pool/BTCKS.h"

// Room user
#include "communication/incoming/room/user/QUIT.h"
#include "communication/incoming/room/user/WALK.h"
#include "communication/incoming/room/user/CHAT.h"
#include "communication/incoming/room/user/SHOUT.h"
#include "communication/incoming/room/user/WAVE.h"

// Room settings
#include "communication/incoming/room/settings/CREATEFLAT.h"
#include "communication/incoming/room/settings/SETFLATINFO.h"
#include "communication/incoming/room/settings/GETFLATCAT.h"
#include "communication/incoming/room/settings/GETFLATINFO.h"
#include "communication/incoming/room/settings/SETFLATCAT.h"
#include "communication/incoming/room/settings/UPDATEFLAT.h"

// Room items
#include "communication/incoming/room/items/PLACESTUFF.h"
#include "communication/incoming/room/items/ADDSTRIPITEM.h"
#include "communication/incoming/room/items/MOVESTUFF.h"
#include "communication/incoming/room/items/FLATPROPBYITEM.h"

// Catalogue
#include "communication/incoming/catalogue/GCIX.h"
#include "communication/incoming/catalogue/GCAP.h"
#include "communication/incoming/catalogue/GRPC.h"
#include "communication/incoming/catalogue/GET_ALIAS_LIST.h"

// Inventory
#include "communication/incoming/inventory/GETSTRIP.h"

// Only allow these headers to be processed if the session is not logged in.
int packet_whitelist[] = { 206, 202, 4, 49, 42, 203, 197, 146, 46, 43 };

/**
 * Assigns all header handlers to this array
 */
void message_handler_init() {
    // Login
    message_requests[206] = INIT_CRYPTO;
    message_requests[202] = GENERATEKEY;
    message_requests[4] = TRY_LOGIN;
    message_requests[49] = GDATE;

    // Register
    message_requests[42] = APPROVENAME;
    message_requests[203] = APPROVE_PASSWORD;
    message_requests[197] = APPROVEEMAIL;
    message_requests[146] = PARENT_EMAIL_REQUIRED;
    message_requests[46] = CHECK_AGE;
    message_requests[43] = REGISTER;

    // User
    message_requests[7] = GET_INFO;
    message_requests[8] = GET_CREDITS;
    message_requests[44] = UPDATE;
    message_requests[149] = UPDATE_ACCOUNT;

    // Messenger
    message_requests[12] = MESSENGERINIT; 
    message_requests[41] = FINDUSER;
    message_requests[40] = MESSENGER_REMOVEBUDDY;
    message_requests[36] = MESSENGER_ASSIGNPERSMSG;
    message_requests[39] = MESSENGER_REQUESTBUDDY;
    message_requests[38] = MESSENGER_DECLINEBUDDY;
    message_requests[37] = MESSENGER_ACCEPTBUDDY;
    message_requests[233] = MESSENGER_GETREQUESTS;
    message_requests[33] = MESSENGER_SENDMSG;
    message_requests[191] = MESSENGER_GETMESSAGES;
    message_requests[32] = MESSENGER_MARKREAD;
    
    // Navigator
    message_requests[150] = NAVIGATE;
    message_requests[16] = SUSERF;
    message_requests[151] = GETUSERFLATCATS;
    message_requests[264] = RECOMMENDED_ROOMS;

    // Room
    message_requests[182] = GETINTERST;
    message_requests[2] = room_directory;
    message_requests[57] = TRYFLAT; // @y1052/123
    message_requests[59] = GOTOFLAT;
    message_requests[126] = GETROOMAD;
    message_requests[60] = G_HMAP;
    message_requests[62] = G_OBJS;
    message_requests[64] = G_STAT;
    message_requests[63] = G_ITEMS;
    message_requests[61] = G_USRS;
    message_requests[213] = GET_FURNI_REVISIONS;
    message_requests[98] = LETUSERIN;

    // Pool
    message_requests[116] = SWIMSUIT;
    message_requests[106] = DIVE;
    message_requests[107] = SPLASHPOSITION;
    message_requests[104] = SIGN;
    message_requests[105] = BTCKS;

    // Room user
    message_requests[53] = QUIT;
    message_requests[75] = WALK;
    message_requests[52] = CHAT;
    message_requests[55] = SHOUT;
    message_requests[94] = WAVE;

    // Room settings
    message_requests[21] = GETFLATINFO;
    message_requests[29] = CREATEFLAT;
    message_requests[25] = SETFLATINFO;
    message_requests[24] = UPDATEFLAT;
    message_requests[152] = GETFLATCAT;
    message_requests[153] = SETFLATCAT;

    // Room items
    message_requests[90] = PLACESTUFF;
    message_requests[73] = MOVESTUFF;

    // Catalogue
    message_requests[101] = GCIX;
    message_requests[102] = GCAP;
    message_requests[215] = GET_ALIAS_LIST;
    message_requests[100] = GRPC;

    // Inventory
    message_requests[65] = GETSTRIP;
    message_requests[67] = ADDSTRIPITEM;
    message_requests[66] = FLATPROPBYITEM;


    /*Client [0.0.0.0] incoming data: 203 / CK@Dalex@F123456
hello!
Client [0.0.0.0] incoming data: 149 / BU@M@C123@H@J07.04.1992@C@F123456*/
}

/**
 * Retrieves the handler by header id
 * @param im the incoming message struct
 * @param player the player struct
 */
void message_handler_invoke(incoming_message *im, session *player) {
    char *preview = strdup(im->data);
    replace_vulnerable_characters(&preview, true, '|');

    printf("Client [%s] incoming data: %i / %s\n", player->ip_address, im->header_id, preview);

    free(preview);

    // Stop the server from crashing
    if (im->header_id > 9999 || im->header_id < 0) {
        return;
    }

    // Don't process any headers it can't find
    if (message_requests[im->header_id] == NULL) {
        return;
    }

    mh_request handle = message_requests[im->header_id];

    if (player->logged_in) {
        handle(player, im);
    } else {

        // If the user isn't logged in, we only process whitelisted headers.
        for (int i = 0; i < (sizeof(packet_whitelist) / sizeof(packet_whitelist[0])); i++) {
            int header_id = packet_whitelist[i];

            if (header_id == im->header_id) {
                handle(player, im);
            }
        }
    }
}