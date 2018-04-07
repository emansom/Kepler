#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "item.h"

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
item *item_create(int room_id, char *class_name, int sprite_id, int x, int y, double z, int rotation, char *custom_data) {
    item *room_item = malloc(sizeof(item));
    room_item->room_id = room_id;
    room_item->class_name = class_name;
    room_item->is_table = 0;
    room_item->sprite_id = sprite_id;
    room_item->x = x;
    room_item->y = y;
    room_item->z = z;
    room_item->rotation = rotation;
    room_item->custom_data = custom_data;
    room_item->current_program = NULL;
    room_item->current_program_state = NULL;
    room_item->can_sit = 0;
    room_item->is_solid = 0;
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
        sb_add_string(om->sb, " ");
        om_write_str(om, room_item->current_program_state);

        room *room = room_manager_get_by_id(room_item->room_id);

        if (room != NULL) {
            room_send(room, om);
        }

    } else {
        room_item->current_program_state = NULL;
    }
}