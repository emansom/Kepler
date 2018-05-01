#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "util/encoding/vl64encoding.h"

void room_directory(session *player, incoming_message *message) {
    char *content = im_get_content(message);
    bool is_public = (content[0] == 'A');

    outgoing_message *om = om_create(19); // "@S"
    player_send(player, om);
    om_cleanup(om);

    if (is_public) {
        char* contents_chopped = content + 1;

        int len;
        int room_id = vl64_decode(contents_chopped, &len);

        room_enter(room_id, player);
    } 

    free(content);
    /*om = om_create(166); // "Bf"
    om_write_raw_str(om, "/client/");
    player_send(session, om);
    om_cleanup(om);*/
}
