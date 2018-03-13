#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "hashtable.h"

#include "communication/messages/outgoing_message.h"
#include "game/player/player.h"

#include "messenger.h"

messenger *messenger_create() {
    messenger *messenger_manager = malloc(sizeof(messenger));
    hashtable_new(&messenger_manager->friends);
    return messenger_manager;
}

void messenger_init(player *player) {
    printf("MESSENGER INIT: %i\n", player->player_data->id);
}