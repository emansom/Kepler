#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

#include "game/messenger/messenger.h"
#include "game/messenger/messenger_friend.h"

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
        messenger_friend *friend;
        list_get_at(p->messenger->friends, i, (void*)&friend);
        
        messenger_serialise(friend->friend_id, response);
    }

    player_send(p, response);
    om_cleanup(response);

}
