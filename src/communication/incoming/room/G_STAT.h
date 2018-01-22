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

    session->room_user->needs_update = 1;
    
    ListIter iter;
    list_iter_init(&iter, room->users);  

    player *room_player;
    outgoing_message *players = om_create(28); // "@\"
    while (list_iter_next(&iter, (void*) &room_player) != CC_ITER_END) {
        append_user_list(players, room_player);
    }

    player_send(session, players);
    om_cleanup(players);

}
