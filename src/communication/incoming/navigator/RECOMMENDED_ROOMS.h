#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"

#include "list.h"

#include "database/queries/rooms/room_query.h"

void RECOMMENDED_ROOMS(session *player, incoming_message *message) {
    List *rooms = room_query_random_rooms(3);
    list_sort_in_place(rooms, room_manager_sort);

    outgoing_message *navigator = om_create(351);
    om_write_int(navigator, (int) list_size(rooms));

    for (size_t i = 0; i < list_size(rooms); i++) {
        room *instance;
        list_get_at(rooms, i, (void *) &instance);

        room_append_data(instance, navigator, player->player_data->id);
    }

    player_send(player, navigator);
    om_cleanup(navigator);

    if (rooms != NULL) {
        for (size_t i = 0; i < list_size(rooms); i++) {
            room *instance;
            list_get_at(rooms, i, (void *) &instance);

            if (room_manager_get_by_id(instance->room_id) == NULL) {
                room_dispose(instance, false);
            }
        }

        list_destroy(rooms);
    }
}


