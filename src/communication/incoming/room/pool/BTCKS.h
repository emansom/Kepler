#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"
#include "game/room/pool/pool_handler.h"

void BTCKS(player *session, incoming_message *message) {
    int mode = im_read_vl64(message);
    char *tickets_for = im_read_str(message);

    if (tickets_for == NULL) {
        return;
    }

    int cost_credits = -1;
    int tickets_amount = -1;

    if (mode == 1) {
        tickets_amount = 2;
        cost_credits = 1;
    } else {
        tickets_amount = 20;
        cost_credits = 6;
    }

    if (!query_player_exists_username(tickets_for)) {
        char alert[130];
        sprintf(alert, "Sorry, but the user you tried to buy %i for doesn't exist in the hotel!<br>The tickets have not been bought.", tickets_amount);
        send_alert(session, alert);
        goto cleanup;
    }

    player_data *data = query_player_data(query_player_id(tickets_for));
    data->tickets += tickets_amount;
    query_player_save_tickets(data->id, data->tickets);

    player *ticket_receiver = player_manager_find_by_name(tickets_for);

    if (ticket_receiver != NULL) {
        if (ticket_receiver->player_data->id != session->player_data->id) {
            char alert[80];
            sprintf(alert, "%s has gifted you tickets!", session->player_data->username);
            send_alert(ticket_receiver, alert);
        }

        player_send_tickets(ticket_receiver);
    }

    player_data_cleanup(data);

    cleanup:
        free(tickets_for);

}
