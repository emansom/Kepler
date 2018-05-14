#include "club_manager.h"

#include "game/moderation/fuserights_manager.h"

#include "communication/messages/outgoing_message.h"
#include "database/queries/player_query.h"

/**
 * Add club to player
 * @param player the player to add club to
 * @param days the amount of days to add
 */
void club_subscribe(session *player, int days) {
    time_t now = time(NULL);

    long day_in_second = 24 * 60 * 60;
    long seconds_to_add = (days * day_in_second);

    if (player->player_data->club_subscribed == 0)
        player->player_data->club_subscribed = now;

    if (player->player_data->club_expiration - now <= 0)
        player->player_data->club_expiration = now + seconds_to_add + (day_in_second - 1);
    else
        player->player_data->club_expiration += seconds_to_add;

    player_query_save_club_information(player);

    // Immediately send club informations to player
    club_refresh(player);
}

/**
 * Refresh club informations
 *
 * TODO: Give furniture to player
 * TODO: HC Badge management
 * @param player: the player to send club informations
 */
void club_refresh(session *player) {
    time_t now = time(NULL);
    int since_months = 0;
    int total_days = 0;
    int remaining_days_for_this_month = 0;
    int prepaid_months = 0;

    if (player->player_data->club_expiration == 0)
        total_days = 0;
    else
        total_days = (int)((player->player_data->club_expiration - now) / 60 / 60 / 24);

    if (total_days < 0)
        total_days = 0;

    if (total_days < 0 && player->player_data->club_subscribed != 0) {
        player->player_data->club_subscribed = 0;
        player_query_save_club_information(player);
    }

    remaining_days_for_this_month = ((total_days - 1) % 31) + 1;
    prepaid_months = (total_days - remaining_days_for_this_month) / 31;

    bool needs_update = false;

    if (player->player_data->club_subscribed == 0) {
        if (player->player_data->rank == 2) {
            player->player_data->rank = 1;
            needs_update = true;
        }
        since_months = 0;
    } else {
        since_months = (int) (now - player->player_data->club_subscribed) / 60 / 60 / 24 / 31;

        if (player->player_data->rank == 1) {
            player->player_data->rank = 2;
            needs_update = true;
        }
    }

    outgoing_message *club_habbo = om_create(7);
    om_write_str(club_habbo, "club_habbo");
    om_write_int(club_habbo, remaining_days_for_this_month);
    om_write_int(club_habbo, since_months);
    om_write_int(club_habbo, prepaid_months);

    // This parameter - when set to 2 - directly opens the Club dialog
    om_write_int(club_habbo, 1);
    player_send(player, club_habbo);
    om_cleanup(club_habbo);

    // Set rank to 2 or 1 if their club expired or so and
    // refresh their fuserights
    if (needs_update) {
        outgoing_message *om = om_create(2); // @B
        fuserights_append(player->player_data->rank, om);
        player_send(player, om);
        om_cleanup(om);

        player_query_save_details(player);
    }
}