#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "item.h"

#include "game/pathfinder/coord.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_manager.h"

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
    room_item->custom_data = custom_data;
    room_item->current_program = NULL;
    room_item->current_program_state = NULL;
    room_item->coords = create_coord(x, y);
    room_item->coords->z = z;
    room_item->coords->rotation = rotation;

    if (definition_id > 0) {

    }

    return room_item;
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