#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_manager.h"

#include "database/queries/player_query.h"

void FINDUSER(player *user, incoming_message *message) {
    if (user->player_data == NULL) {
        return;
    }

    //im_read_b64(message);

    char *input_search = im_read_str(message);

    int search_id = query_player_id(input_search);
    player *search_player = player_manager_find_by_id(search_id);
    player_data *data = NULL;

    printf("user id: %i\n", search_id);

    if (search_player != NULL) {
        data = search_player->player_data;
    } else {
        data = query_player_data(search_id);
    }

    outgoing_message *msg = om_create(128); // "B@"
    om_write_str(msg, "MESSENGER");

    if (data != NULL) {
        om_write_int(msg, data->id);
        om_write_str(msg, data->username);
        om_write_int(msg, strcmp(data->sex, "M") == 0);
        om_write_str(msg, data->console_motto);

        int is_online = (search_player != NULL);
         om_write_int(msg, is_online);

        if (is_online) {
            if (search_player->room_user->room_id > 0) {
                room *room = room_manager_get_by_id(search_player->room_user->room_id);

                if (list_size(room->public_items) > 0) {
                    om_write_str(msg, room->room_data->name);
                } else {
                    om_write_str(msg, "Floor1a");
                }
            }
        } else {
            om_write_str(msg, "Hotel View");
        }

        om_write_char(msg, 2);
        om_write_str(msg, "00-00-0000");
        om_write_str(msg, data->figure);

    } else {
        om_write_int(msg, 0);
    }

    player_send(user, msg);
    om_cleanup(msg);

    free(input_search);
}
