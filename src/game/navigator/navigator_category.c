#include <stdlib.h>
#include <string.h>

#include "game/room/room.h"

#include "communication/messages/outgoing_message.h"
#include "game/player/player.h"

#include "navigator_category.h"

/**
 * Create a navigator category.
 * 
 * @param id the category id
 * @param parent_id the parent id
 * @param name the name of the category
 * @param public_spaces the public spaces
 * @param allow_trading does this category allow trading
 * @return the created navigator navigator
 */
room_category *category_create(int id, int parent_id, char *name, int public_spaces, int allow_trading, int minrole_access, int minrole_setflatcat) {
    room_category *category = malloc(sizeof(room_category));
    category->id = id;
    category->parent_id = parent_id;
    category->name = strdup(name);
    category->public_spaces = public_spaces;
    category->allow_trading = allow_trading;

    if (public_spaces == 0) {
        category->category_type = PRIVATE;
    } else {
        category->category_type = PUBLIC;
    }

    category->minrole_access = minrole_access;
    category->minrole_setflatcat = minrole_setflatcat;

    return category;
}

int category_has_access(room_category *category, int rank) {
    return rank >= category->minrole_access;
}

/**
 * Serialise rooms on the navigator.
 * 
 * @param navigator the outgoing message
 * @param instance the room instance
 * @param category_type the category type
 */
void category_serialise(outgoing_message *navigator, room *instance, room_category_type category_type, player *player) {
    om_write_int(navigator, instance->room_data->id); // room id

    if (category_type == PUBLIC) {
        om_write_int(navigator, 1);
        om_write_str(navigator, instance->room_data->name);
        om_write_int(navigator, instance->room_data->visitors_now); // current visitors
        om_write_int(navigator, instance->room_data->visitors_max); // max vistors
        om_write_int(navigator, instance->room_data->category); // category id
        om_write_str(navigator, instance->room_data->description); // description
        om_write_int(navigator, instance->room_data->id); // room id
        om_write_int(navigator, 0);
        om_write_str(navigator, instance->room_data->ccts);
        om_write_int(navigator, 0);
        om_write_int(navigator, 1);
    }

    if (category_type == PRIVATE) {
        om_write_str(navigator, instance->room_data->name);
        
        if (player->player_data->id == instance->room_data->owner_id || instance->room_data->show_name == 1) {
            om_write_str(navigator, instance->room_data->owner_name); // room owner
        } else {
            om_write_str(navigator, "-"); // room owner
        }

        if (instance->room_data->accesstype == 2) {
            om_write_str(navigator, "password");
        }

        if (instance->room_data->accesstype == 1) {
            om_write_str(navigator, "closed");
        }

        if (instance->room_data->accesstype == 0) {
            om_write_str(navigator, "open");
        }

        om_write_int(navigator, instance->room_data->visitors_now); // current visitors
        om_write_int(navigator, instance->room_data->visitors_max); // max vistors
        om_write_str(navigator, instance->room_data->description); // description
    }
}