#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "database/queries/messenger_query.h"

void MESSENGER_REQUESTBUDDY(player *session, incoming_message *message) {
    char *input_search = im_read_str(message);
    int search_id = query_player_id(input_search);

    if (search_id == -1) {
        goto cleanup;
        return;
    }

    if (messenger_is_friends(session->messenger, search_id)) {
        goto cleanup;
        return;
    }

    if (!messenger_query_new_request(session->player_data->id, search_id)) {
        goto cleanup;
        return;
    }

    player *requested_player = player_manager_find_by_id(search_id);

    if (requested_player != NULL) {
        outgoing_message *response = om_create(132); // "BD"
        om_write_int(response, session->player_data->id);
        om_write_str(response, session->player_data->username);
        player_send(requested_player, response);
        om_cleanup(response);
    }

    cleanup:
        free(input_search);
}
