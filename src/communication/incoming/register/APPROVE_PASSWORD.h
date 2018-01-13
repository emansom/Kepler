#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "shared.h"

int valid_password(const char *username, const char *password) {
    char *temp_username = strdup(username);
    char *temp_password = strdup(username);

    int error_code = 0;

    if (strcmp(strlwr(temp_username), strlwr(temp_password)) > 0) {
        error_code = 5;
    } else if (strlen(password) < 6) {
        error_code = 1;
    } else if (strlen(password) > 10) {
        error_code = 2;
    } else if (!has_numbers(password)) {
        error_code = 4;
    }

    free(temp_password);
    free(temp_username);

    return error_code;
}

void APPROVE_PASSWORD(player *player, incoming_message *message) {
    char *username = im_read_str(message);
    char *password = im_read_str(message);

    outgoing_message *om = om_create(282); // "DZ"
    om_write_int(om, valid_password(username, password));
    player_send(player, om);
    om_cleanup(om);
}
