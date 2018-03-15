#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "database/queries/messenger_query.h"

void MESSENGER_DECLINEBUDDY(player *session, incoming_message *message) {
    im_read_vl64(message); // useless?

    int friend_id = im_read_vl64(message);

    messenger_query_delete_request(session->player_data->id, friend_id);
    messenger_query_delete_request(friend_id, session->player_data->id);

    player *friend = player_manager_find_by_id(friend_id);

    if (friend != NULL) {
        messenger_remove_request(friend->messenger, friend_id);
    }
}
