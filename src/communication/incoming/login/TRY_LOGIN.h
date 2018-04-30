#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

void TRY_LOGIN(session *player, incoming_message *message) {
    char *username = im_read_str(message);
    char *password = im_read_str(message);

    if (username != NULL || password != NULL) {
        int player_id = player_query_login(username, password);

        if (player_id == -1) {
            send_localised_error(player, "login incorrect");
            return;
        } else {
            player_data *data = player_query_data(player_id);
            player->player_data = data;
        }

        player_manager_destroy_session_by_id(player_id);
        player_login(player);
    }

    free(username);
    free(password);
}
