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

        om_write_int(navigator, instance->room_data->id); // rooms id
        om_write_str(navigator, instance->room_data->name);

        if (player->player_data->id == instance->room_data->owner_id || instance->room_data->show_name == 1) {
            om_write_str(navigator, instance->room_data->owner_name); // rooms owner
        } else {
            om_write_str(navigator, "-"); // rooms owner
        }

        if (instance->room_data->accesstype == 2) {
            om_write_str(navigator, "password");
        }

        if (instance->room_data->accesstype == 1) {
            om_write_str(navigator, "closed");
        }

        if (instance->room_data->accesstype == 0) {
            om_write_str(navigator, "open");
        }

        om_write_int(navigator, instance->room_data->visitors_now); // current visitors
        om_write_int(navigator, instance->room_data->visitors_max); // max vistors
        om_write_str(navigator, instance->room_data->description); // description
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


