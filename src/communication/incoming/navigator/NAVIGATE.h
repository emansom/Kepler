#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void NAVIGATE(player *player, incoming_message *message) {
    /*printf("1: %i\n", im_read_vl64(message));
    printf("2: %i\n", im_read_vl64(message));
    printf("3: %i\n", im_read_vl64(message));*/

    int hide_full = im_read_vl64(message);
    int category_id = im_read_vl64(message);

    outgoing_message *navigator = om_create(220); // "C\"

    om_write_int(navigator, hide_full);
    om_write_int(navigator, 3);
    om_write_int(navigator, 0); // show public spaces
    om_write_str(navigator, "Public Rooms");
    om_write_int(navigator, 0); // current visitors
    om_write_int(navigator, 25); // max visitors
    om_write_int(navigator, 0); // parent id

    om_write_int(navigator, 1); // room id
    om_write_int(navigator, 1);
    om_write_str(navigator, "Welcome Lounge");
    om_write_int(navigator, 0); // current visitors
    om_write_int(navigator, 25); // max vistors
    om_write_int(navigator, 3); // category id
    om_write_str(navigator, "welcome_lounge"); // description
    om_write_int(navigator, 1); // room id
    om_write_int(navigator, 0);
    om_write_str(navigator, "hh_room_nlobby");
    om_write_int(navigator, 0);
    om_write_int(navigator, 1);

    /*om_write_int(navigator, 1); // room id
    om_write_int(navigator, 0); // 0 if public space
    om_write_str(navigator, "Welcome Lounge");
    om_write_int(navigator, 0); // current visitors
    om_write_int(navigator, 25); // max vistors
    om_write_int(navigator, 3); // category id
    om_write_str(navigator, "welcome_lounge"); // description
    om_write_int(navigator, 1); // room id
    om_write_int(navigator, 0);
    om_write_str(navigator, "hh_room_nlobby");
    om_write_int(navigator, 0);
    om_write_int(navigator, 1);*/

    player_send(player, navigator);
    om_cleanup(navigator);

}
