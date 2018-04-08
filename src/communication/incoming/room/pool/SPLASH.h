#include <game/pathfinder/coord.h>
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

#include "game/room/pool/pool_handler.h"

#include "game/room/mapping/room_tile.h"
#include "game/room/mapping/room_map.h"

void SPLASH(player *player, incoming_message *message) {
    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    if (strstr(content, ",") == NULL) {
        goto cleanup;
    }

    char *content_x = get_argument(content, ",", 0);
    char *content_y = get_argument(content, ",", 1);

    if (!is_numeric(content_x) || !is_numeric(content_y)) {
        free(content_x);
        free(content_y);
        goto cleanup;
    }

    coord walk_destination = {};
    walk_destination.x = (int) strtol(content_x, NULL, 10);
    walk_destination.y = (int) strtol(content_y, NULL, 10);

    room_user *room_entity = (room_user *) player->room_user;
    room_tile *tile = room_entity->room->room_map->map[room_entity->current->x][room_entity->current->y];

    room_entity->current->x = walk_destination.x;
    room_entity->current->y = walk_destination.y;
    room_entity->current->z = room_entity->room->room_data->model_data->heights[room_entity->current->x][room_entity->current->y];
    room_entity->walking_lock = false;

    // Immediately update status
    outgoing_message *players = om_create(34); // "@b
    append_user_status(players, player);
    room_send(player->room_user->room, players);

    room_user_add_status(room_entity, "swim", "", -1, "", 0, 0);
    walk_to(room_entity, 20, 19);

    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        if (strcmp(item->class_name, "poolLift") == 0) {
            item_assign_program(item, "open");
        }
    }

    free(content_x);
    free(content_y);

    cleanup:
        free(content);
}
