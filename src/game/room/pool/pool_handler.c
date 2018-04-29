#include <stdio.h>
#include <string.h>
#include <util/stringbuilder.h>

#include "deque.h"

#include "communication/messages/outgoing_message.h"
#include "database/queries/player_query.h"

#include "pool_handler.h"

#include "game/items/item.h"
#include "game/items/definition/item_definition.h"

#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "game/pathfinder/pathfinder.h"
#include "game/pathfinder/coord.h"

#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

void pool_warp_swim(session*, item*, coord warp, bool exit);

/**
 * Handle walking out of booth
 *
 * @param player the player to handle
 */
void pool_booth_exit(session *player) {
    // Open up booth
    room_tile *tile = player->room_user->room->room_map->map[player->room_user->current->x][player->room_user->current->y];

    if (tile != NULL && tile->highest_item != NULL) {
        item *item = tile->highest_item;

        if (strcmp(item->definition->sprite, "poolBooth") == 0) {
            item_assign_program(item, "open");
            player->room_user->walking_lock = false;
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
void pool_item_walk_on(session *p, item *item) {
    room_user *room_entity = (room_user*)p->room_user;

    if (strcmp(item->definition->sprite, "poolLift") == 0) {
        item_assign_program(item, "close");

        char target[200];
        sprintf(target, "targetcamera %i", p->room_user->instance_id);

        outgoing_message *target_diver = om_create(71); // "AG"
        sb_add_string(target_diver->sb, "cam1");
        sb_add_string(target_diver->sb, " ");
        sb_add_string(target_diver->sb, target);
        room_send((room *) room_entity->room, target_diver);

        room_entity->walking_lock = true;

        outgoing_message *om = om_create(125); // "A}"
        player_send((session *) room_entity->player, om);
        om_cleanup(om);

        room_entity->player->player_data->tickets--;
        session_send_tickets((session*)room_entity->player);
        player_query_save_currency((session *) room_entity->player);

    }

    if (strcmp(item->definition->sprite, "poolBooth") == 0) {
        item_assign_program(item, "close");
        room_entity->walking_lock = true;

        outgoing_message *om = om_create(96); // "A`"
        player_send((session *) room_entity->player, om);
        om_cleanup(om);
    }

    if (strcmp(room_entity->room->room_data->model_data->model_name, "pool_b") == 0) {
        if (strcmp(item->definition->sprite, "queue_tile2") == 0) {
            coord next;
            coord_get_front(item->position, &next);
            walk_to(room_entity, next.x, next.y);
        }
    }


    if (strcmp(item->definition->sprite, "poolEnter") == 0) {
        coord warp = { };

        if (item->position->x == 20 && item->position->y == 28) {
            warp.x = 21;
            warp.y = 28;
        }

        if (item->position->x == 17 && item->position->y == 21) {
            warp.x = 17;
            warp.y = 22;
        }

        if (item->position->x == 31 && item->position->y == 10) {
            warp.x = 31;
            warp.y = 11;
        }

        pool_warp_swim(p, item, warp, false);
    }

    if (strcmp(item->definition->sprite, "poolExit") == 0) {
        coord warp = { };

        if (item->position->x == 21 && item->position->y == 28) {
            warp.x = 20;
            warp.y = 28;
        }

        if (item->position->x == 17 && item->position->y == 22) {
            warp.x = 17;
            warp.y = 21;
        }

        if (item->position->x == 20 && item->position->y == 19) {
            warp.x = 19;
            warp.y = 19;
        }

        if (item->position->x == 31 && item->position->y == 11) {
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
void pool_warp_swim(session *p, item *item, coord warp, bool exit) {
    room_user *room_entity = (room_user*)p->room_user;
    stop_walking(room_entity, true);

    room_entity->current->x = warp.x;
    room_entity->current->y = warp.y;
    room_entity->current->z = room_entity->room->room_map->map[warp.x][warp.y]->tile_height;

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
    if (strcmp(public_item->definition->sprite, "poolBooth") == 0) {
        if (public_item->position->x == 17 && public_item->position->y == 11) {
            room->room_map->map[18][11]->highest_item = public_item;
        }

        if (public_item->position->x == 17 && public_item->position->y == 9) {
            room->room_map->map[18][9]->highest_item = public_item;
        }

        if (public_item->position->x == 8 && public_item->position->y == 1) {
            room->room_map->map[8][0]->highest_item = public_item;
        }

        if (public_item->position->x == 9 && public_item->position->y == 1) {
            room->room_map->map[9][0]->highest_item = public_item;
        }
    }
}