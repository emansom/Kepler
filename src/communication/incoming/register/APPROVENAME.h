#include "util/stringbuilder.h"
#include "lib/dyad/dyad.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

int valid_username(char *username) {
    char *allowed_chars = "1234567890qwertyuiopasdfghjklzxcvbnm-+=?!@:.,$";
    int valid = 0;

    for (int j = 0; j < strlen(username); j++) {
        valid = 0;

        for (int i = 0; i < strlen(allowed_chars); i++) {
            if (username[j] == allowed_chars[i]) {
                valid = 1;
                continue;
            }
        }
    }

    return valid;
}

int get_name_check_code(char *username) {
    if (strlen(username) > 15 || !valid_username(username)) {
        return 2;
    } else {
        if (query_player_exists_username(username)) {
            return 4;
        } else {
            return 0;
        }
    }
}

void APPROVENAME(player *player, incoming_message *message) {
    char *username = im_read_str(message);
    int name_check_code = get_name_check_code(username);

    printf("username: %s - %i\n", username, name_check_code);

    outgoing_message *om = om_create(36); // "@d"
    om_write_int(om, name_check_code);
    player_send(player, om);
    om_cleanup(om);
}