#include <game/room/room_user.h>
#include "list.h"
#include "roller_task.h"

#include "game/pathfinder/coord.h"

#include "game/room/mapping/room_map.h"
#include "game/room/mapping/room_tile.h"

#include "game/player/player.h"

#include "util/stringbuilder.h"

void do_roller_task(room *room) {
    bool regenerate_map = false;

    for (size_t i = 0; i < list_size(room->items); i++) {
        item *roller;
        list_get_at(room->items, i, (void *) &roller);

        if (!roller->definition->behaviour->is_roller) {
            continue;
        }

        List *process;
        list_new(&process);

        room_tile *item_tile = room->room_map->map[roller->coords->x][roller->coords->y];

        if (item_tile == NULL) {
            continue;
        }

        for (size_t j = 0; j < list_size(item_tile->items); j++) {
            item *item;
            list_get_at(item_tile->items, j, (void *) &item);

            if (do_roller_item(room, roller, item)) {
                regenerate_map = true;
            }
        }

        if (item_tile->entity != NULL) {
            do_roller_player(room, roller, item_tile->entity);
        }
    }

    if (regenerate_map) {
        room_map_regenerate(room);
    }
}

bool do_roller_item(room *room, item *roller, item *item) {
    if (item->id == roller->id) {
        return false;
    }

    if (item->coords->z < roller->coords->z) {
        return false;
    }

    coord front;
    coord_get_front(roller->coords, &front);

    if (!room_tile_is_walkable(room, NULL, front.x, front.y)) {
        return false;
    }

    room_tile *front_tile = room->room_map->map[front.x][front.y];
    double next_height = front_tile->tile_height;

    if (front_tile->highest_item != NULL) {
        if (!front_tile->highest_item->definition->behaviour->is_roller) {
            if (item->definition->behaviour->can_stack_on_top && item->definition->stack_height == front_tile->highest_item->definition->stack_height) {
                next_height -= item->definition->stack_height;
            }
        }
    }

    if (item->item_below != NULL) {
        if (!item->item_below->definition->behaviour->is_roller) {
            next_height = item->coords->z;

            bool subtract_roller_height = false;

            if (front_tile->highest_item != NULL) {
                if (!front_tile->highest_item->definition->behaviour->is_roller) {
                    subtract_roller_height = true;
                }
            } else {
                subtract_roller_height = true;
            }

            if (subtract_roller_height) {
                next_height -= roller->definition->stack_height;
            }
        }
    }

    outgoing_message *om = om_create(230);
    om_write_int(om, item->coords->x);
    om_write_int(om, item->coords->y);
    om_write_int(om, front.x);
    om_write_int(om, front.y);
    om_write_int(om, 1);
    om_write_int(om, item->id);
    sb_add_float_delimeter(om->sb, item->coords->z, 2);
    sb_add_float_delimeter(om->sb, next_height, 2);
    om_write_int(om, roller->id);
    room_send(room, om);

    item->coords->x = front.x;
    item->coords->y = front.y;
    item->coords->z = next_height;

    return true;
}

void do_roller_player(room *room, item *roller, room_user *room_entity) {
    if (room_entity->is_walking) {
        return;
    }

    if (room_entity->current->z < roller->coords->z) {
        return;
    }

    coord front;
    coord_get_front(roller->coords, &front);

    if (!room_tile_is_walkable(room, room_entity, front.x, front.y)) {
        return;
    }

    room_tile *previous_tile = room->room_map->map[room_entity->current->x][room_entity->current->y];
    room_tile *front_tile = room->room_map->map[front.x][front.y];
    double next_height = front_tile->tile_height;

    previous_tile->entity = NULL;
    front_tile->entity = room_entity;

    outgoing_message *om = om_create(230);
    om_write_int(om, room_entity->current->x);
    om_write_int(om, room_entity->current->y);
    om_write_int(om, front.x);
    om_write_int(om, front.y);

    om_write_int(om, 0);
    om_write_int(om, roller->id);
    om_write_int(om, 2);
    om_write_int(om, room_entity->instance_id);

    sb_add_float_delimeter(om->sb, room_entity->current->z, 2);
    sb_add_float_delimeter(om->sb, next_height, 2);
    room_send(room, om);

    room_user_invoke_item(room_entity);
    room_entity->current->x = front.x;
    room_entity->current->y = front.y;
    room_entity->current->z = next_height;

}
