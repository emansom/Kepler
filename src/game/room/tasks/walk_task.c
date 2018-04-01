#include "walk_task.h"

#include "list.h"
#include "deque.h"

#include "game/player/player.h"

#include "game/pathfinder/coord.h"
#include "game/pathfinder/rotation.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/items/item.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

#include "communication/messages/outgoing_message.h"

#include "shared.h"

void process_user(player *player);

/**
 * Walk task cyle that is called every 500ms
 *
 * @param room the room handled
 */
void walk_task(room *room) {
    List *users;
    list_copy_shallow(room->users, &users);

    int user_updates = 0;
    outgoing_message *status_update = om_create(34); // "@b"

    for (size_t i = 0; i < list_size(users); i++) {
        player *room_player;
        list_get_at(users, i, (void*)&room_player);

        if (room_player == NULL) {
            continue;
        }

        if (room_player->room_user == NULL) {
            continue;
        }

        process_user(room_player);

        if (room_player->room_user->needs_update) {
            room_player->room_user->needs_update = 0;
            user_updates++;
            append_user_status(status_update, room_player);
        }

    }

    if (user_updates > 0) {
        room_send(room, status_update);
    } else {
        om_cleanup(status_update);
    }

    list_destroy(users);
}

/**
 * Process the user in the walk task cycle
 *
 * @param player the player struct to process
 */
void process_user(player *player) {
    room_user *user = (void*)player->room_user;

    if (user->is_walking) {
        if (user->next != NULL) {
            user->current->x = user->next->x;
            user->current->y = user->next->y;
            user->current->z = user->next->z;
            free(user->next);
        }

        if (deque_size(user->walk_list) > 0) {
            coord *next;
            deque_remove_first(user->walk_list, (void*)&next);
            next->z = user->room->room_data->model_data->heights[next->x][next->y];

            char value[30];
            sprintf(value, " %i,%i,%.2f", next->x, next->y, next->z);

            int rotation = calculate(user->current->x, user->current->y, next->x, next->y);
            user->body_rotation = rotation;
            user->head_rotation = rotation;

            room_user_add_status(user, "mv", value, -1, "", 0, 0);
            user->next = next;


        } else {
            user->next = NULL;
            user->is_walking = 0;
            //room_tile *tile = user->room->room_map->map[user->current->x][user->current->y];
            stop_walking(user);
        }

        player->room_user->needs_update = 1;
    }
}