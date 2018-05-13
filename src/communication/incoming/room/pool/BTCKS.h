#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

void BTCKS(session *player, incoming_message *message) {
    int mode = im_read_vl64(message);
    char *tickets_for = im_read_str(message);

    if (tickets_for == NULL) {
        return;
    }

    int cost_credits;
    int tickets_amount;

    if (mode == 1) {
        tickets_amount = 2;
        cost_credits = 1;
    } else {
        tickets_amount = 20;
        cost_credits = 6;
    }

    if (player->player_data->credits < cost_credits) {
        outgoing_message *om = om_create(68); // "AD"
        player_send(player, om);
        om_cleanup(om);
        goto cleanup;
    }

    if (!player_query_exists_username(tickets_for)) {
        outgoing_message *om = om_create(76); // "AL"
        sb_add_string(om->sb, tickets_for); // No user named <here> found. Gift not purchased.
        player_send(player, om);
        om_cleanup(om);
        goto cleanup;
    }

    player_data *data = player_query_data(player_query_id(tickets_for));
    data->tickets += tickets_amount;
    player_query_save_tickets(data->id, data->tickets);

    session *ticket_receiver = player_manager_find_by_name(tickets_for);

    if (ticket_receiver != NULL) {
        if (ticket_receiver->player_data->id != player->player_data->id) {
            char alert[80];
            sprintf(alert, "%s has gifted you tickets!", player->player_data->username);
            player_send_alert(ticket_receiver, alert);
        }

        ticket_receiver->player_data->tickets = data->tickets;
        player_refresh_tickets(ticket_receiver);
    }

    player->player_data->credits -= cost_credits;
    player_query_save_currency(player);
    player_refresh_credits(player);

    room_user_reset_idle_timer(player->room_user);
    player_data_cleanup(data);

    cleanup:
    free(tickets_for);

}
