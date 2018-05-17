#include "log.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void update_badge(room_user *room_user) {
    outgoing_message *badge_notify = om_create(228); // "Cd"

    om_write_int(badge_notify, room_user->instance_id);

    if (strlen(room_user->entity->details->active_badge) > 0) {
        om_write_str(badge_notify, room_user->entity->details->active_badge);
    }

    room_send(room_user->room, badge_notify);
    om_cleanup(badge_notify);
}

void SETBADGE(entity *player, incoming_message *im) {
    char *new_badge = im_read_str(im);
    int show_badge = im_read_vl64(im);

    if (show_badge == 1) {
        // TODO: validate if user actually has badge
        Array *badges = player_query_badges(player->details->id);

        // Return if player doesn't own this badge
        if (array_contains(badges, &new_badge) == 0) {
            log_debug("User doesn't have badge %s", new_badge);
            return;
        }

        player->details->active_badge = strdup(new_badge);
    } else {
        player->details->active_badge = "";
    }

    free(new_badge);

    update_badge(player->room_user);
    player_query_save_active_badge(player);

    log_debug("Player has badge and saved to database %s", player->details->active_badge);
}
