#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

void message_login(player *player, incoming_message *message) {
    char *username = im_read_str(message);
    char *password = im_read_str(message);

    int player_id = query_player_login(username, password);

    if (player_id == -1) {
        send_localised_error(player, "login incorrect");
        return;
    }

    player->player_data = query_player_data(player_id);

    outgoing_message *om;
    om = om_create(2); // @B
    player_send(player, om);
    om_cleanup(om);

    om = om_create(3); // @C
    player_send(player, om);
    om_cleanup(om);

    char welcome_message[150];
    sprintf(welcome_message, "Welcome to the Kepler server, %s!", player->player_data->username);

    om = om_create(139); // @C
    om_write_str(om, welcome_message);
    player_send(player, om);
    om_cleanup(om);

    free(username);
    free(password);
}
