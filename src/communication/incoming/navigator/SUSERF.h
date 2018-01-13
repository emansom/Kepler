#include "util/stringbuilder.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void SUSERF(player *player, incoming_message *message) {

    outgoing_message *om = om_create(16); // "@P"
    om_write_int_delimeter(om, 1, 9);
    om_write_str_delimeter(om, "Alex's Room", 9);
    om_write_str_delimeter(om, "Alex", 9);
    om_write_str_delimeter(om, "open", 9);
    om_write_str_delimeter(om, "x", 9);
    om_write_str_delimeter(om, "0", 9);
    om_write_str_delimeter(om, "25", 9);
    om_write_str_delimeter(om, "null", 9);
    om_write_str_delimeter(om, "The room of Alex", 9);
    om_write_str_delimeter(om, "The room of Alex", 9);
    om_write_char(om, 13);
    player_send(player, om);
    om_cleanup(om);
       /* mResponse.AppendString(room.mID, 9);
        mResponse.AppendString(room.mName, 9);
        mResponse.AppendString(room.mOwnerName, 9);
        mResponse.AppendString(room.mState, 9);
        mResponse.AppendString('x', 9);
        mResponse.AppendString(room.Visitors, 9);
        mResponse.AppendString(room.mMaxVisitors, 9);
        mResponse.AppendString("null", 9);
        mResponse.AppendString(room.mDescription, 9);
        mResponse.AppendString(room.mDescription, 9);
        mResponse.AppendChar(13);*/


}
