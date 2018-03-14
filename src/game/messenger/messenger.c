#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "list.h"

#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

#include "game/messenger/messenger.h"
#include "game/messenger/messenger_friend.h"

#include "database/queries/messenger_query.h"

messenger *messenger_create() {
    messenger *messenger_manager = malloc(sizeof(messenger));
    messenger_manager->friends = NULL;
    return messenger_manager;
}

void messenger_init(player *player) {
    List *friends = messenger_query_get_friends(player->player_data->id);
    player->messenger->friends = friends;

    if (list_size(player->messenger->friends) > 0) {
        messenger_friend *friend;
        list_get_at(player->messenger->friends, 0, (void*)&friend);
        printf("friend found %i\n", friend->friend_id);
    }
}

void messenger_cleanup(messenger *messenger_manager) {
    if (messenger_manager->friends != NULL) {
        list_destroy(messenger_manager->friends);
    }

    free(messenger_manager);
}