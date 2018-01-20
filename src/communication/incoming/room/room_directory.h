#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "util/encoding/vl64encoding.h"

void room_directory(player *player, incoming_message *message) {
    char *content = im_get_content(message);
    bool is_public = (content[0] == 'A');

    outgoing_message *om = om_create(19); // "@S"
    player_send(player, om);
    om_cleanup(om);

    if (is_public) {
        memmove(content, content+1, strlen(content)); // remove first character

        int len;
        int room_id = vl64_decode(content, &len);

        room *room = room_manager_get_by_id(room_id);

        if (room != NULL) {
            room_enter(room, player);
            room_load(room, player);
        }
    } 

    free(content);
    /*om = om_create(166); // "Bf"
    om_write_raw_str(om, "/client/");
    player_send(player, om);
    om_cleanup(om);*/
}
