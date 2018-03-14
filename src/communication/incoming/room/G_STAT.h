#include "game/player/player.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"
#include "game/room/mapping/room_model.h"

#include "list.h"

void G_STAT(player *session, incoming_message *message) {
    if (session->room_user->room == NULL) {
        return;
    }
    
    room *room = session->room_user->room;
    
    if (room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", session->room_user->room->room_data->id);
        return;
    }
    
    outgoing_message *players = om_create(34); // "@b"
    
    for (int i = 0; i < list_size(room->users); i++) {
        player *room_player;
        list_get_at(room->users, i, (void*)&room_player);
        append_user_status(players, room_player);
    }

    player_send(session, players);
    om_cleanup(players);

    session->room_user->needs_update = 1;
}
