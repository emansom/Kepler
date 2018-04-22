#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"
#include "game/room/pool/pool_handler.h"

void SIGN(session *player, incoming_message *message) {
    char *vote = im_get_content(message);

    if (vote == NULL) {
        return;
    }

    room_user *room_entity = (room_user *) player->room_user;

    if (room_entity->room == NULL) {
        goto cleanup;
    }

    if (!is_numeric(vote)) {
        goto cleanup;
    }

    int voting_id = (int) strtol(vote, NULL, 10);

    if (voting_id < 0) {
        goto cleanup;
    }

    if (voting_id <= 7) { // Lido voting
        room_entity->lido_vote = (voting_id + 3);
    }

    char vote_id[11];
    sprintf(vote_id, " %i", voting_id);

    room_user_add_status(room_entity, "sign", vote_id, 5, "", 0, 0);
    room_entity->needs_update = true;

    cleanup:
        free(vote);
}
