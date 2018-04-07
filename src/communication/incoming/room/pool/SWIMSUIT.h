#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

#include "game/pathfinder/coord.h"
#include "game/items/item.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

void SWIMSUIT(player *player, incoming_message *message) {
    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    if (player->room_user->room == NULL) {
        goto cleanup;
    }

    free(player->player_data->pool_figure);
    player->player_data->pool_figure = strdup(content);

    query_player_save_looks(player);

    // Refresh pool figure
    outgoing_message *refresh = om_create(28); // "@\"
    append_user_list(refresh, player);
    room_send(player->room_user->room, refresh);

    // Open up booth
    room_tile *tile = player->room_user->room->room_map->map[player->room_user->current->x][player->room_user->current->y];

    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        if (strcmp(item->class_name, "poolBooth") == 0) {
            item_assign_program(item, "open");
        }
    }

    // Handle walking out of pool
    if (strcmp(player->room_user->room->room_data->model_data->model_name, "pool_a") == 0) {
        // Walk out of the booth
        if (player->room_user->current->y == 11) {
            walk_to(player->room_user, 19, 11);
        } else if (player->room_user->current->y == 9) {
            walk_to(player->room_user, 19, 9);
        }
    }

    // Handle walking out of wobble squabble area
    if (strcmp(player->room_user->room->room_data->model_data->model_name, "md_a") == 0) {
        // Walk out of the booth
        if (player->room_user->current->x == 8) {
            walk_to(player->room_user, 8, 2);
        } else if (player->room_user->current->x == 9) {
            walk_to(player->room_user, 9, 2);
        }
    }

    cleanup:
        free(content);
}
