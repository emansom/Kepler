#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/messenger/messenger.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "list.h"

void MESSENGERINIT(player *p, incoming_message *message) {
    if (p->player_data == NULL) {
        return;
    }
    
    outgoing_message *response = om_create(12); // "@L"
    om_write_str(response, p->player_data->console_motto);
    om_write_int(response, 600);
    om_write_int(response, 200);
    om_write_int(response, 600);
    om_write_int(response, list_size(p->messenger->friends)); // Buddy list count

    for (int i = 0; i < list_size(p->messenger->friends); i++) {
        int friend_id;
        list_get_at(p->messenger->friends, i, (void*)&friend_id);

        player_data *data = player_manager_get_data_by_id(friend_id);
        player *search_player = player_manager_find_by_id(friend_id);

        printf("friendid: %i\n", friend_id);

        if (data != NULL) {
            om_write_int(response, data->id);
            om_write_str(response, data->username);
            om_write_int(response, strcmp(data->sex, "M") == 0);
            om_write_str(response, data->console_motto);

            int is_online = (search_player != NULL);
            om_write_int(response, is_online);

            if (is_online) {
                if (search_player->room_user->room_id > 0) {
                    room *room = room_manager_get_by_id(search_player->room_user->room_id);

                    if (list_size(room->public_items) > 0) {
                        om_write_str(response, room->room_data->name);
                    } else {
                        om_write_str(response, "Floor1a");
                    }
                }
            } else {
                om_write_str(response, "Hotel View");
            }

            om_write_char(response, 2);
            om_write_str(response, "00-00-0000");
            om_write_str(response, data->figure);
        }
    }

    player_send(p, response);
    om_cleanup(response);

}
