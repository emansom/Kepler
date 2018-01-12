#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void GETFLATINFO(player *player, incoming_message *message) {
    char *content = im_get_content(message);

    if (!is_numeric(content)) {
        return;
    }

    int room_id = atoi(content);

    /*mResponse.Init(54); // "@v"
    mResponse.AppendWiredBoolean(room.mAllHaveRights);
    mResponse.AppendWired((int)room.mState);
    mResponse.AppendWired(room.mID);
    mResponse.AppendString(room.mShowOwnerName ? room.mOwnerName : "-"); // replace owner name with - if not show
    mResponse.AppendString(room.mModel);
    mResponse.AppendString(room.mName);
    mResponse.AppendString(room.mDescription);
    mResponse.AppendWiredBoolean(room.mShowOwnerName);
    mResponse.AppendWiredBoolean(room.GetCategory().mHasTrading);
    mResponse.AppendWired(room.Visitors);
    mResponse.AppendWired(room.mMaxVisitors);
    SendResponse();*/

    outgoing_message *flat_info = om_create(54); // "@v"
    om_write_int(flat_info, 0); // flat all rights
    om_write_int(flat_info, 0); // room state
    om_write_int(flat_info, room_id); // room id

    om_write_str(flat_info, "Alex"); // room owner
    om_write_str(flat_info, "model_a"); // room model
    om_write_str(flat_info, "Alex's Room"); // room name
    om_write_str(flat_info, "The room of Alex"); // room description

    om_write_int(flat_info, 1); // show owner name
    om_write_int(flat_info, 1); // has trading
    om_write_int(flat_info, 0); // current visitors
    om_write_int(flat_info, 25); // max visitors
    player_send(player, flat_info);
    om_cleanup(flat_info);

    free(content);
}
