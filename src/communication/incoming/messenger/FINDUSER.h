#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "database/queries/player_query.h"

void FINDUSER(player *user, incoming_message *message) {
    if (user->player_data == NULL) {
        return;
    }

    char *input_search = im_read_str(message);

    int search_id = query_player_id(input_search);
    player_data *data = player_manager_get_data_by_id(search_id);

    outgoing_message *msg = om_create(128); // "B@"
    om_write_str(msg, "MESSENGER");

    if (data != NULL) {
        messenger_serialise(data->id, msg);

    } else {
        om_write_int(msg, 0);
    }

    player_send(user, msg);
    om_cleanup(msg);

    free(input_search);
}
