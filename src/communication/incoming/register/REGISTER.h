#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

#include "shared.h"

void REGISTER(player *player, incoming_message *message) {
    im_read_b64_int(message);
    char *name = im_read_str(message);

    im_read_b64_int(message);
    char *figure = im_read_str(message);

    im_read_b64_int(message);
    char *gender = im_read_str(message);

    im_read_b64_int(message);
    im_read_b64_int(message);

    // don't give a shit about emails
    im_read_b64_int(message);
    free(im_read_str(message));

    // couldn't give a shit about your birthday either
    im_read_b64_int(message);
    free(im_read_str(message));

    im_read(message, 11);
    char *password = im_read_str(message);

    bool kick_user = false;

    if (valid_password(name, password) == 0 && get_name_check_code(name) == 0) {
        player_query_create(name, figure, gender, password);
    } else {
        kick_user = true;
    }

    free(name);
    free(figure);
    free(gender);
    free(password);
}
