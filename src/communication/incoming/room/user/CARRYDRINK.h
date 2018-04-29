#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

enum drink_type {
    DRINK,
    EAT,
    ITEM
};


void CARRYDRINK(session *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    enum drink_type drinks[26];
    drinks[1] = DRINK;  // Tea
    drinks[2] = DRINK;  // Juice
    drinks[3] = EAT;    // Carrot
    drinks[4] = EAT;    // Ice-cream
    drinks[5] = DRINK;  // Milk
    drinks[6] = DRINK;  // Blackcurrant
    drinks[7] = DRINK;  // Water
    drinks[8] = DRINK;  // Regular
    drinks[9] = DRINK;  // Decaff
    drinks[10] = DRINK; // Latte
    drinks[11] = DRINK; // Mocha
    drinks[12] = DRINK; // Macchiato
    drinks[13] = DRINK; // Espresso
    drinks[14] = DRINK; // Filter
    drinks[15] = DRINK; // Iced
    drinks[16] = DRINK; // Cappuccino
    drinks[17] = DRINK; // Java
    drinks[18] = DRINK; // Tap
    drinks[19] = DRINK; // H*bbo Cola
    drinks[20] = ITEM;  // Camera
    drinks[21] = EAT;   // Hamburger
    drinks[22] = DRINK; // Lime H*bbo Soda
    drinks[23] = DRINK; // Beetroot H*bbo Soda
    drinks[24] = DRINK; // Bubble juice from 1999
    drinks[25] = DRINK; // Lovejuice

    int drink_id = (int) strtol(content, NULL, 10);

    if (drink_id >= 0 && drink_id <= 25) {
        char *carry_status[8];
        char *use_status[8];

        char drink_as_string[11];
        sprintf(drink_as_string, " %i", drink_id);

        enum drink_type type = drinks[drink_id];

        if (type == DRINK) {
            strcpy((char*)carry_status, "carryd");
            strcpy((char*)use_status, "drink");
        }

        if (type == EAT) {
            strcpy((char*)carry_status, "carryf");
            strcpy((char*)use_status, "eat");
        }

        if (type == ITEM) {
            strcpy((char*)carry_status, "cri");
            strcpy((char*)use_status, "usei");
        }

        room_user_remove_status(player->room_user, "cri");
        room_user_remove_status(player->room_user, "carryf");
        room_user_remove_status(player->room_user, "carryd");

        room_user_add_status(player->room_user, strdup((char*) carry_status), drink_as_string, 120, (char*)use_status, 12, 1);
        player->room_user->needs_update = true;
    }
}