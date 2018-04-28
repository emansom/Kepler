#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/room/room_user.h"

#include "game/pathfinder/rotation.h"
#include "game/pathfinder/coord.h"

void LOOKTO(session *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char *content = im_get_content(message);

    char *str_x = NULL;
    char *str_y = NULL;

    if (content == NULL) {
        return;
    }

    str_x = get_argument(content, " ", 0);
    str_y = get_argument(content, " ", 1);

    int towards_x = (int) strtol(str_x, NULL, 10);
    int towards_y = (int) strtol(str_y, NULL, 10);

    room_user *room_entity = (room_user *) player->room_user;

    int rotation = calculate_human_direction(room_entity->current->x, room_entity->current->y, towards_x, towards_y);
    coord_set_rotation(room_entity->current, rotation, rotation);
    room_entity->needs_update = true;

    free(content);
    free(str_x);
    free(str_y);
        
}
