#include <stdio.h>
#include <string.h>

#include "deque.h"

#include "communication/messages/outgoing_message.h"

#include "pool_handler.h"
#include "game/items/item.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/pathfinder/pathfinder.h"
#include "game/pathfinder/coord.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

void pool_warp_swim(player*, item*, coord warp, bool exit);

/**
 * Handle walking out of booth
 *
 * @param player the player to handle
 */
void pool_booth_exit(player *player) {
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
            walk_to((room_user*) player->room_user, 19, 11);
        } else if (player->room_user->current->y == 9) {
            walk_to((room_user*) player->room_user, 19, 9);
        }
    }

    // Handle walking out of wobble squabble area
    if (strcmp(player->room_user->room->room_data->model_data->model_name, "md_a") == 0) {
        // Walk out of the booth
        if (player->room_user->current->x == 8) {
            walk_to((room_user*) player->room_user, 8, 2);
        } else if (player->room_user->current->x == 9) {
            walk_to((room_user*) player->room_user, 9, 2);
        }
    }
}

/**
 * Handle walking on a pool item.
 *
 * @param player the player to handle
 */
void pool_item_walk_on(player *p, item *item) {
    room_user *room_entity = (room_user*)p->room_user;

    if (strcmp(item->class_name, "poolLift") == 0) {
        item_assign_program(item, "close");

        room_entity->walking_lock = true;
        room_entity->player->player_data->tickets--;

        outgoing_message *om = om_create(125); // "A}"
        player_send((player*)room_entity->player, om);
        om_cleanup(om);

        player_send_tickets((player*)room_entity->player);
    }

    if (strcmp(item->class_name, "poolBooth") == 0) {
        item_assign_program(item, "close");

        outgoing_message *om = om_create(96); // "A`"
        player_send((player*)room_entity->player, om);
        om_cleanup(om);
    }


    if (strcmp(item->class_name, "poolEnter") == 0) {
        coord warp = { };

        if (item->x == 20 && item->y == 28) {
            warp.x = 21;
            warp.y = 28;
        }

        if (item->x == 17 && item->y == 21) {
            warp.x = 17;
            warp.y = 22;
        }

        if (item->x == 31 && item->y == 10) {
            warp.x = 31;
            warp.y = 11;
        }

        pool_warp_swim(p, item, warp, false);
    }

    if (strcmp(item->class_name, "poolExit") == 0) {
        coord warp = { };

        if (item->x == 21 && item->y == 28) {
            warp.x = 20;
            warp.y = 28;
        }

        if (item->x == 17 && item->y == 22) {
            warp.x = 17;
            warp.y = 21;
        }

        if (item->x == 20 && item->y == 19) {
            warp.x = 19;
            warp.y = 19;
        }

        if (item->x == 31 && item->y == 11) {
            warp.x = 31;
            warp.y = 10;
        }

        pool_warp_swim(p, item, warp, true);
    }
}

/**
 * Warp to the pool/ladder and remove/add swim state.
 *
 * @param p the player to warp
 * @param item the warp they're sending the state (such as splash) to clients
 * @param warp the coordinates to warp to
 * @param exit true or false whether they're exiting or entering
 */
void pool_warp_swim(player *p, item *item, coord warp, bool exit) {
    room_user *room_entity = (room_user*)p->room_user;
    stop_walking(room_entity, true);

    room_entity->current->x = warp.x;
    room_entity->current->y = warp.y;
    room_entity->current->z = room_entity->room->room_data->model_data->heights[warp.x][warp.y];

    if (!exit) {
        room_user_add_status(room_entity, "swim", "", -1, "", 0, 0);
    } else {
        room_user_remove_status(room_entity, "swim");
    }

    item_assign_program(item, "");
    room_entity->needs_update = true;
}

/**
 * Setup pool item redirections across mutiple tiles.
 *
 * @param room the room to add the item to.
 * @param public_item the item to add
 */
void pool_setup_redirections(room *room, item *public_item) {
    if (strcmp(public_item->class_name, "poolBooth") == 0) {
        if (public_item->x == 17 && public_item->y == 11) {
            room->room_map->map[18][11]->highest_item = public_item;
        }

        if (public_item->x == 17 && public_item->y == 9) {
            room->room_map->map[18][9]->highest_item = public_item;
        }

        if (public_item->x == 8 && public_item->y == 1) {
            room->room_map->map[8][0]->highest_item = public_item;
        }

        if (public_item->x == 9 && public_item->y == 1) {
            room->room_map->map[9][0]->highest_item = public_item;
        }
    }
}