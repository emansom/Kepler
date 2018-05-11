#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/room/room_user.h"

void SETBADGE(session *player, incoming_message *im) {
    char *new_badge = im_read_str(im); // TODO: check if user owns badge
    int show_badge = im_read_vl64(im);

    if (show_badge == 1) {
        player->player_data->active_badge = strdup(new_badge);
    } else {
        player->player_data->active_badge = "";
    }

    free(new_badge);

    room_user_update_badge(player->room_user);

    // TODO: save to database
}
