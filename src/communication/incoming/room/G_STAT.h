#include <game/room/room_model.h>
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void G_STAT(player *p, incoming_message *message) {
    if (p->room_user->room == NULL) {
        return;
    }

    if (p->room_user->room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", p->room_user->room->room_data->id);
        return;
    }

    room *room = p->room_user->room;

    ListIter iter;
    list_iter_init(&iter, room->users);
    player *user;

    outgoing_message *players = om_create(34); // "@b"
    while (list_iter_next(&iter, (void*) &user) != CC_ITER_END) {
        append_user_status(players, user);
    }
    player_send(p, players);
    om_cleanup(players);

    players = om_create(34); // "@b"
    append_user_status(players, p);
    room_send(room, players);
    om_cleanup(players);
}
