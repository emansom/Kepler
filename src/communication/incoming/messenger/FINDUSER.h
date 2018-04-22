#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "database/queries/player_query.h"

void FINDUSER(session *user, incoming_message *message) {
    if (user->player_data == NULL) {
        return;
    }

    char *input_search = im_read_str(message);
    int search_id = player_query_id(input_search);

    outgoing_message *msg = om_create(128); // "B@"
    om_write_str(msg, "MESSENGER");

    if (search_id > 0) {
        messenger_entry_serialise(search_id, msg);

    } else {
        om_write_int(msg, 0);
    }

    session_send(user, msg);
    om_cleanup(msg);

    free(input_search);
}
