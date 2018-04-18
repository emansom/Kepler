#include <game/pathfinder/coord.h>
#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

#include "game/room/pool/pool_handler.h"

#include "game/room/mapping/room_tile.h"
#include "game/room/mapping/room_map.h"

void SPLASHPOSITION(player *diver, incoming_message *message) {
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

    room_user *room_entity = (room_user *) diver->room_user;
    room_tile *tile = room_entity->room->room_map->map[room_entity->current->x][room_entity->current->y];

    room_entity->current->x = walk_destination.x;
    room_entity->current->y = walk_destination.y;
    room_entity->current->z = room_entity->room->room_data->model_data->heights[room_entity->current->x][room_entity->current->y];
    room_entity->walking_lock = false;

    // Immediately update status
    room_user_add_status(room_entity, "swim", "", -1, "", 0, 0);

    outgoing_message *players = om_create(34); // "@b
    append_user_status(players, diver);
    room_send(diver->room_user->room, players);

    // Walk to ladder exit
    walk_to(room_entity, 20, 19);

    // Open up lift
    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        if (strcmp(item->definition->sprite, "poolLift") == 0) {
            item_assign_program(item, "open");
        }
    }

    int total = 0, sum = 0;
    double final = 0;

    // Count votes
    for (size_t i = 0; i < list_size(room_entity->room->users); i++) {
        player * room_player;
        list_get_at(room_entity->room->users, i, (void *) &room_player);

        if (room_player->player_data->id == diver->player_data->id) {
            continue;
        }

        if (room_player->room_user->lido_vote > 0) {
            sum += room_player->room_user->lido_vote;
            total++;
        }
    }

    char target[200];
    sprintf(target, "targetcamera %i", diver->room_user->instance_id);

    outgoing_message *target_diver = om_create(71); // "AG"
    sb_add_string(target_diver->sb, "cam1");
    sb_add_string(target_diver->sb, " ");
    sb_add_string(target_diver->sb, target);
    room_send((room *) room_entity->room, target_diver);

    // Show diving score
    if (total > 0) {
        final = sum / total;

        char score_text[200];
        sprintf(score_text, "showtext %s's score:/%.1f", diver->player_data->username, final);

        outgoing_message *score_message = om_create(71); // "AG"
        sb_add_string(score_message->sb, "cam1");
        sb_add_string(score_message->sb, " ");
        sb_add_string(score_message->sb, score_text);
        room_send((room *) room_entity->room, score_message);
    }

    // Reset all diving scores
    for (size_t i = 0; i < list_size(room_entity->room->users); i++) {
        player *room_player;
        list_get_at(room_entity->room->users, i, (void *) &room_player);

        if (room_player->room_user->lido_vote > 0) {
            room_player->room_user->lido_vote = -1;
        }
    }

    free(content_x);
    free(content_y);

    cleanup:
        free(content);
}
