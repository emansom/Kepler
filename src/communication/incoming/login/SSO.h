#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"
#include "game/player/player_manager.h"

void SSO(entity *player, incoming_message *message) {
    char *ticket = im_read_str(message);

    if (ticket == NULL) {
        return;
    }

    int player_id = player_query_sso(ticket);

    if (player_id == -1) {
        player_send_localised_error(player, "Incorrect SSO ticket");
    } else {
        entity_data *data = player_query_data(player_id);
        player->details = data;

        player_manager_destroy_session_by_id(player_id);
        player_login(player);
    }

    free(ticket);
}
