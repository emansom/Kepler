#include "stdlib.h"
#include "stdio.h"

#include "list.h"
#include "database/queries/item_query.h"

#include "room_tile.h"
#include "room_map.h"

#include "game/pathfinder/coord.h"
#include "game/pathfinder/affected_tiles.h"

#include "game/items/item.h"
#include "game/items/definition/item_definition.h"

#include "game/room/room.h"
#include "game/room/mapping/room_model.h"
#include "game/room/pool/pool_handler.h"

#include "communication/messages/outgoing_message.h"

#include "util/stringbuilder.h"

void room_map_add_items(room *room);

/**
 * Sort list by heights.
 *
 * @param e1 the first item
 * @param e2 the second item
 * @return whether to sort
 */
int cmp(void const *e1, void const *e2) {
    item *i = (*((item**) e1));
    item *j = (*((item**) e2));

    if (i->coords->z < j->coords->z)
        return -1;
    if (i->coords->z == j->coords->z)
        return 0;

    return 1;
}

/**
 * Initalises the room map for the furniture collision.
 *
 * @param room the room instance
 */
void room_map_init(room *room) {
    if (room->room_map == NULL) {
        room->room_map = malloc(sizeof(room_map));

        for (int x = 0; x < room->room_data->model_data->map_size_x; x++) {
            for (int y = 0; y < room->room_data->model_data->map_size_y; y++) {
                room->room_map->map[x][y] = room_tile_create(room, x, y);
            }
        }
    }

    room_map_regenerate(room);
}
/**
 * Regenerates the room map.
 *
 * @param room the room instance
 */
void room_map_regenerate(room *room) {
    for (int x = 0; x < room->room_data->model_data->map_size_x; x++) { 
        for (int y = 0; y < room->room_data->model_data->map_size_y; y++) { 
            room_tile *tile = room->room_map->map[x][y];
            room_tile_reset(tile, room);
        }
    }

    room_map_add_items(room);
}


/**
 * Add items to the room collision map.
 *
 * @param room the room instance
 */
void room_map_add_items(room *room) {
    list_sort_in_place(room->items, cmp);

    for (size_t i = 0; i < list_size(room->items); i++) {
        item *item;
        list_get_at(room->items, i, (void*)&item);

        if (item->definition->behaviour->is_wall_item) {
            continue;
        }

        room_tile *tile = room->room_map->map[item->coords->x][item->coords->y];

        if (tile == NULL) {
            continue;
        }

        room_tile_add_item(tile, item);

        if (item->definition->behaviour->is_public_space_object
            || tile->tile_height < item_total_height(item)) {

            tile->tile_height = item_total_height(item);
            tile->highest_item = item;

            List *affected_tiles = get_affected_tiles(item->definition->length, item->definition->width, item->coords->x, item->coords->y, item->coords->rotation);

            for (size_t j = 0; j < list_size(affected_tiles); j++) {
                coord *pos;
                list_get_at(affected_tiles, j, (void*)&pos);

                if (pos->x == item->coords->x && pos->y == item->coords->y) {
                    continue;
                }

                room_tile *affected_tile = room->room_map->map[pos->x][pos->y];

                if (affected_tile == NULL) {
                    continue;
                }

                affected_tile->tile_height = item_total_height(item);
                affected_tile->highest_item = item;

                free(pos);
            }

            list_destroy(affected_tiles);
        }

        if (item->definition->behaviour->is_public_space_object) {
            pool_setup_redirections(room, item);
        }
    }
}

/**
 * Add a specific item to the room map
 *
 * @param room the room to add the map item to
 * @param item the item to add
 */
void room_map_add_item(room *room, item *item) {
    item->room_id = room->room_id;
    list_add(room->items, item);



    if (item->definition->behaviour->is_wall_item) {
        char *item_str = item_as_string(item);

        outgoing_message *om = om_create(83); // "AS"
        sb_add_string(om->sb, item_str);
        room_send(room, om);

        free(item_str);
    } else {
        room_map_item_adjustment(room, item, false);
        room_map_regenerate(room);

        char *item_str = item_as_string(item);

        outgoing_message *om = om_create(93); // "A]"
        sb_add_string(om->sb, item_str);
        room_send(room, om);

        free(item_str);
    }

    item_query_save(item);
}

void room_map_move_item(room *room, item *item, bool rotation) {
    item->room_id = room->room_id;

    if (!item->definition->behaviour->is_wall_item) {
        room_map_item_adjustment(room, item, rotation);
        room_map_regenerate(room);

        char *item_str = item_as_string(item);

        outgoing_message *om = om_create(95); // "A_"
        sb_add_string(om->sb, item_str);
        room_send(room, om);

        free(item_str);
    } else {

    }

    item_query_save(item);
}

void room_map_remove_item(room *room, item *item) {
    item->room_id = room->room_id;
    list_remove(room->items, item, (void*)&item);


    if (item->definition->behaviour->is_wall_item) {
        outgoing_message *om = om_create(84); // "AT"
        sb_add_int(om->sb, item->id);
        room_send(room, om);
    } else {
        room_map_regenerate(room);
        char *item_str = item_as_string(item);

        outgoing_message *om = om_create(94); // "A^"
        sb_add_string(om->sb, item_str);
        room_send(room, om);

        free(item_str);
    }

    item->room_id = 0;
    item->coords->x = 0;
    item->coords->y = 0;
    item->coords->z = 0;

    item_query_save(item);
}

/**
 * Handle item adjustment.
 *
 * @param moveItem the item
 * @param rotation the rotation only
 */
void room_map_item_adjustment(room *room, item *item, bool rotation) {
    if (rotation) {

   } else {
        room_tile *tile = room->room_map->map[item->coords->x][item->coords->y];

        if (tile == NULL) {
            return;
        }

        item->coords->z = tile->tile_height;
    }
    /*if (rotation) {
        for (Item item : this.getTile(moveItem.getPosition().getX(), moveItem.getPosition().getY()).getItems()) {
            if (item.getId() == moveItem.getId()) {
                continue;
            }

            if (item.getPosition().getZ() < moveItem.getPosition().getZ()) {
                continue;
            }

            item.getPosition().setRotation(moveItem.getPosition().getRotation());
            item.updateStatus();
        }
    } else {
        moveItem.getPosition().setZ(this.getTileHeight(moveItem.getPosition().getX(), moveItem.getPosition().getY()));
    }*/
}

/**
 * Destroy the room map instance.
 *
 * @param room the room instance
 */
void room_map_destroy(room *room) {
    if (room->room_map != NULL) {
        for (int x = 0; x < room->room_data->model_data->map_size_x; x++) {
            for (int y = 0; y < room->room_data->model_data->map_size_y; y++) {
                room_tile_destroy(room->room_map->map[x][y], room);
                room->room_map->map[x][y] = NULL;
            }
        }

        free(room->room_map);
        room->room_map = NULL;
    }

}