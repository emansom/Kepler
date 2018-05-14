#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void SUBSCRIBE_CLUB(session *player, incoming_message *message) {
    free(im_read_str(message));
    int selection = im_read_vl64(message);

    if (selection == 1) {
        if (player->player_data->credits < 25) {
            return;
        }

        player->player_data->credits -= 25;
        club_subscribe(player, 31);
        player_query_save_currency(player);

    } else if (selection == 2) {
        if (player->player_data->credits < 60) {
            return;
        }

        player->player_data->credits -= 60;
        club_subscribe(player, 93);
        player_query_save_currency(player);

    } else if (selection == 3) {
        if (player->player_data->credits < 105) {
            return;
        }

        player->player_data->credits -= 105;
        club_subscribe(player, 186);
        player_query_save_currency(player);
    }

    player_refresh_credits(player);
}