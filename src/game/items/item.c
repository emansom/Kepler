#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "item.h"

#include "game/pathfinder/coord.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_manager.h"
#include "game/items/item_manager.h"

#include "communication/messages/outgoing_message.h"


#include "util/stringbuilder.h"

/**
 * Create room item struct.
 *
 * @param room_id the room id of the item
 * @param class_name the item class name
 * @param sprite_id the item sprite name
 * @param x the item x coordinate
 * @param y the item y coordinate
 * @param z the item z coordinate
 * @param rotation the item rotation
 * @param custom_data the item custom data
 * @return
 */
item *item_create(int id, int room_id, int definition_id, int x, int y, double z, int rotation, char *custom_data) {
    item *room_item = malloc(sizeof(item));
    room_item->id = id;
    room_item->room_id = room_id;

    if (custom_data == NULL) {
        custom_data = strdup("");
    }

    room_item->custom_data = custom_data;
    room_item->current_program = NULL;
    room_item->current_program_state = NULL;
    room_item->coords = create_coord(x, y);
    room_item->coords->z = z;
    room_item->coords->rotation = rotation;

    if (definition_id > 0) {
        room_item->definition = item_manager_get_definition_by_id(definition_id);
    }

    return room_item;
}

char *item_as_string(item *item) {
    stringbuilder *sb = sb_create();

    if (item->definition->behaviour->is_wall_item) {


    } else {
        if (!item->definition->behaviour->is_public_space_object) {
            sb_add_int_delimeter(sb, item->id, 2);
            sb_add_string_delimeter(sb, item->definition->sprite, 2);
            sb_add_wired(sb, item->coords->x);
            sb_add_wired(sb, item->coords->y);
            sb_add_wired(sb, item->definition->length);
            sb_add_wired(sb, item->definition->width);
            sb_add_wired(sb, item->coords->rotation);
            sb_add_float_delimeter(sb, item->coords->z, 2);
            sb_add_string_delimeter(sb, item->definition->colour, 2);
            sb_add_string_delimeter(sb, "", 2);
            sb_add_wired(sb, 0);
            sb_add_string_delimeter(sb, item->custom_data, 2);
        } else {
            sb_add_string_delimeter(sb, item->custom_data, ' ');
            sb_add_string_delimeter(sb, item->definition->sprite, ' ');
            sb_add_int_delimeter(sb, item->coords->x, ' ');
            sb_add_int_delimeter(sb, item->coords->y, ' ');
            sb_add_int_delimeter(sb, (int) item->coords->z, ' ');
            sb_add_int(sb, item->coords->rotation);
            sb_add_char(sb, 13);
        }
    }

    char *str = strdup(sb->data);
    sb_cleanup(sb);

    return str;
}

/**
 * Get the string used for packets to append for the hand.
 *
 * @param item the item to append
 * @param strip_slot_id it's strip slot id
 * @return the string to append
 */
char *item_strip_string(item *item, int strip_slot_id) {
    stringbuilder *sb = sb_create();

    sb_add_string_delimeter(sb, "SI", 30);
    sb_add_int_delimeter(sb, item->id, 30);
    sb_add_int_delimeter(sb, strip_slot_id, 30);

    if (item->definition->behaviour->is_wall_item) {
        sb_add_string_delimeter(sb, "I", 30);
    } else {
        sb_add_string_delimeter(sb, "S", 30);
    }

    sb_add_int_delimeter(sb, strip_slot_id, 30);
    sb_add_string_delimeter(sb, item->definition->sprite, 30);

    if (item->definition->behaviour->is_wall_item) {
        sb_add_string_delimeter(sb, item->custom_data, 30);
        sb_add_string_delimeter(sb, "0", 30);
    } else {
        sb_add_int_delimeter(sb, item->definition->length, 30);
        sb_add_int_delimeter(sb, item->definition->width, 30);
        sb_add_string_delimeter(sb, item->custom_data, 30);
        sb_add_string_delimeter(sb, item->definition->colour, 30);
        sb_add_string_delimeter(sb, "0", 30);
        sb_add_string_delimeter(sb, item->definition->sprite, 30);
    }

    sb_add_string(sb, "/");

    char *str = strdup(sb->data);
    sb_cleanup(sb);

    return str;
}

/**
 * Assign program, used for many public rooms.
 *
 * @param room_item the item to assign
 * @param program_state the state of the item
 */
void item_assign_program(item *room_item, char *program_state) {
    if (room_item->current_program_state != NULL) {
        free(room_item->current_program_state);
    }

    if (program_state != NULL) {
        room_item->current_program_state = strdup(program_state);

        outgoing_message *om = om_create(71); // "AG"
        sb_add_string(om->sb, room_item->current_program);

        if (strlen(room_item->current_program_state) > 0) {
            sb_add_string(om->sb, " ");
            om_write_str(om, room_item->current_program_state);
        }

        room *room = room_manager_get_by_id(room_item->room_id);

        if (room != NULL) {
            room_send(room, om);
        }

    } else {
        room_item->current_program_state = NULL;
    }
}

double item_total_height(item *item) {
    double height = item->coords->z + item->definition->stack_height;
    return height;
}

/**
 * Dispose item.
 *
 * @param item the item to dispose
 */
void item_dispose(item *item) {
    free(item->custom_data);
    free(item->coords);

    if (item->current_program != NULL) {
        free(item->current_program);
    }

    if (item->current_program_state != NULL) {
        free(item->current_program_state);
    }

    free(item);
}