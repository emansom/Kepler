#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "database/queries/player_query.h"

#include "game/messenger/messenger.h"
#include "game/messenger/messenger_friend.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "list.h"

void MESSENGERINIT(player *p, incoming_message *message) {
    if (p->player_data == NULL) {
        return;
    }
    
    outgoing_message *friends_list = om_create(12); // "@L"
    om_write_str(friends_list, p->player_data->console_motto);
    om_write_int(friends_list, 600);
    om_write_int(friends_list, 200);
    om_write_int(friends_list, 600);
    om_write_int(friends_list, list_size(p->messenger->friends)); // Buddy list count

    for (int i = 0; i < list_size(p->messenger->friends); i++) {
        messenger_entry *friend;
        list_get_at(p->messenger->friends, i, (void*)&friend);
        messenger_entry_serialise(friend->friend_id, friends_list);
    }

    player_send(p, friends_list);
    om_cleanup(friends_list);

    for (int i = 0; i < list_size(p->messenger->requests); i++) {
        messenger_entry *request_entry;
        list_get_at(p->messenger->requests, i, (void*)&request_entry);

        char *friends_name = query_player_username(request_entry->friend_id);

        if (friends_name == NULL) {
            continue;
        }
        
        outgoing_message *request = om_create(132); // "BD"
        om_write_int(request, request_entry->friend_id);
        om_write_str(request, friends_name);
        player_send(p, request);
        om_cleanup(request);
        free(friends_name);
    }
}
