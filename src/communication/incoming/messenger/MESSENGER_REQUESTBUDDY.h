#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/messenger_query.h"

void MESSENGER_REQUESTBUDDY(session *player, incoming_message *message) {
    char *input_search = im_read_str(message);
    int search_id = player_query_id(input_search);

    if (search_id == -1) {
        goto cleanup;
        return;
    }

    if (messenger_is_friends(player->messenger, search_id)) {
        print_info("debug 0");
        goto cleanup;
        return;
    }

    if (!messenger_query_new_request(player->player_data->id, search_id)) {
        print_info("debug 1");
        goto cleanup;
        return;
    }

    session *requested_player = player_manager_find_by_id(search_id);

    if (requested_player != NULL) {
        outgoing_message *response = om_create(132); // "BD"
        om_write_int(response, player->player_data->id);
        om_write_str(response, player->player_data->username);
        session_send(requested_player, response);
        om_cleanup(response);

        list_add(requested_player->messenger->requests, messenger_entry_create(player->player_data->id));
    }

    cleanup:
        free(input_search);
}
