#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

void TRY_LOGIN(session *player, incoming_message *message) {
    char *username = im_read_str(message);
    char *password = im_read_str(message);

    int player_id = player_query_login(username, password);

    if (player_id == -1) {
        send_localised_error(player, "login incorrect");
        return;
    } else {
        player_data *data = player_query_data(player_id);
        player->player_data = data;
    }

    player_login(player);

    outgoing_message *fuserights_message = om_create(2); // @B
    om_write_str(fuserights_message, "default\2fuse_login\2fuse_buy_credits\2fuse_trade\2fuse_room_queue_default\2fuse_performance_panel");
    player_send(player, fuserights_message);
    om_cleanup(fuserights_message);

    outgoing_message *authenticate_message = om_create(3); // @C
    player_send(player, authenticate_message);
    om_cleanup(authenticate_message);

    char greeting[50];
    sprintf(greeting, "Welcome to the Kepler server, %s!", player->player_data->username);
    send_alert(player, greeting);

    free(username);
    free(password);
}
