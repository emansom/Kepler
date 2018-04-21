#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room_user.h"
#include "game/room/room_manager.h"

void TRYFLAT(player *player, incoming_message *message) {
    int room_id = 0;
    
    char *content = im_get_content(message);
    char *password = NULL;

    if (strstr(content, "/") != NULL) {
        char *temp = get_argument(content, "/", 0);

        if (!is_numeric(temp)) {
            free(temp);
            goto cleanup;
        }

        room_id = (int)strtol(content, NULL, 10);
        password = get_argument(content, "/", 1);

        free(temp);
    } else {
        if (!is_numeric(content)) {
            goto cleanup;
        }
    }

    room *room = room_manager_get_by_id(room_id);

    if (room != NULL) { 
        outgoing_message *interest = om_create(41); // "@i"
        player_send(player, interest);
        om_cleanup(interest);
    }

    cleanup:
        free(content);
		free(password);
}
