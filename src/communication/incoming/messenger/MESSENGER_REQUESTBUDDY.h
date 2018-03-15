#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "database/queries/messenger_query.h"

void MESSENGER_REQUESTBUDDY(player *player, incoming_message *message) {
    char *input_search = im_read_str(message);
    int search_id = query_player_id(input_search);

    print_info("debug: %s\n", input_search);

    if (search_id == -1) {
        goto cleanup;
        return;
    }

    print_info("debug 2: %s\n", input_search);

    if (messenger_is_friends(player->messenger, search_id)) {
        goto cleanup;
        return;
    }

    print_info("debug3: %s\n", input_search);

    messenger_query_new_request(player->player_data->id, search_id);



    cleanup:
        free(input_search);
}
