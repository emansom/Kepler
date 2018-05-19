#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room.h"
#include "game/room/mapping/room_model.h"
#include "game/room/room_user.h"
#include "game/room/manager/room_entity_manager.h"
#include "game/player/player_manager.h"
#include "game/player/player.h"

void FOLLOW_FRIEND(entity *player, incoming_message *message) {
    int target_id = im_read_vl64(message);

    entity *player_friend = player_manager_find_by_id(target_id);

    if (player_friend != NULL && player_friend->room_user->room != NULL) {
        bool is_flat = player_friend->room_user->room->room_data->owner_id == 0;

        outgoing_message *stalk_ok = om_create(286); // "D^"
        om_write_int(stalk_ok, is_flat);
        om_write_int(stalk_ok, player_friend->room_user->room_id);
        player_send(player, stalk_ok);
        om_cleanup(stalk_ok);
    } else {
        outgoing_message *stalk_err = om_create(349); // "E]"
        om_write_int(stalk_err, 2); // probably error ID?
        player_send(player, stalk_err);
        om_cleanup(stalk_err);
     }
}
