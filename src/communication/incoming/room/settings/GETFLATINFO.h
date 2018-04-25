#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void GETFLATINFO(session *player, incoming_message *message) {
    char *content = im_get_content(message);
    bool dispose_after = false;

    if (!is_numeric(content)) {
        goto cleanup;
    }

    int room_id = (int) strtol(content, NULL, 10);
    room *room = room_manager_get_by_id(room_id);

    if (room == NULL) {
        dispose_after = true;
        room = room_query_get_by_room_id(room_id);
    }

    if (room == NULL) {
        goto cleanup;
    }

    outgoing_message *flat_info = om_create(54); // "@v"
    om_write_int(flat_info, room->room_data->superusers); // flat all rights
    om_write_int(flat_info, room->room_data->accesstype); // room state
    om_write_int(flat_info, room->room_data->id); // room id

    if (player->player_data->id == room->room_data->owner_id || room->room_data->show_name == 1) {
        om_write_str(flat_info, room->room_data->owner_name); // room owner
    } else {
        om_write_str(flat_info, "-"); // room owner
    }

    om_write_str(flat_info, room->room_data->model); // room model
    om_write_str(flat_info, room->room_data->name); // room name
    om_write_str(flat_info, room->room_data->description); // room description
    om_write_int(flat_info, room->room_data->show_name); // show owner name
    om_write_int(flat_info, 1); // has trading
    om_write_int(flat_info, room->room_data->visitors_now); // current visitors
    om_write_int(flat_info, room->room_data->visitors_max); // max visitors
    player_send(player, flat_info);
    om_cleanup(flat_info);

    if (dispose_after) {
        room_dispose(room, false);
    }

    cleanup:
    free(content);
}
