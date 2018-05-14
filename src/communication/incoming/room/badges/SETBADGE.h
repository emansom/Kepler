#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void update_badge(room_user *room_user) {
    outgoing_message *badge_notify = om_create(228); // "Cd"

    om_write_int(badge_notify, room_user->instance_id);

    if (strlen(room_user->player->player_data->active_badge) > 0) {
        om_write_str(badge_notify, room_user->player->player_data->active_badge);
    }

    room_send(room_user->room, badge_notify);
    om_cleanup(badge_notify);
}

void SETBADGE(session *player, incoming_message *im) {
    char *new_badge = im_read_str(im); // TODO: check if user owns badge
    int show_badge = im_read_vl64(im);

    if (show_badge == 1) {
        player->player_data->active_badge = strdup(new_badge);
    } else {
        player->player_data->active_badge = "";
    }

    free(new_badge);

    update_badge(player->room_user);

    // TODO: save to database
}
