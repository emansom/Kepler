#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

void message_login(player *player, incoming_message *message) {
    char *username = im_read_str(message);
    char *password = im_read_str(message);

    player_data *player_data = query_player_login(username, password);

    if (player_data == NULL) {
        send_localised_error(player, "login incorrect"/*": Wrong username"*/);
        return;
    }

    printf("Username: %s and password: %s\n", username, password);

    outgoing_message *om;

    om = om_create(2); // @B
    player_send(player, om);
    om_cleanup(om);

    om = om_create(3); // @C
    player_send(player, om);
    om_cleanup(om);

    free(username);
    free(password);
}
