#include "shared.h"

#include "list.h"
#include "hashtable.h"

#include "game/room/room.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/room_query.h"
#include "navigator_category_manager.h"

/**
 * Navigator category manager
 */
void category_manager_init() {
    list_new(&global.room_category_manager.categories);
    room_query_get_categories();
}

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
room_category *category_manager_create(int id, int parent_id, char *name, int public_spaces, int allow_trading) {
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

    return category;
}

/**
 * Add a navigator category.
 * 
 * @param category the category struct
 */
void category_manager_add(room_category *category) {
    list_add(global.room_category_manager.categories, category);
}

/**
 * Get navigator category by the category id.
 * 
 * @param category_id the category id
 * @return the room category
 */
room_category *category_manager_get_by_id(int category_id) {
    ListIter iter;
    list_iter_init(&iter, global.room_category_manager.categories);

    room_category *category;

    while (list_iter_next(&iter, (void*) &category) != CC_ITER_END) {
        if (category->id == category_id) {
            return category;
        }
    }

    return NULL;
}

/**
 * Get child navigator categories by the parent category id.
 * 
 * @param category_id the category id
 * @return the list of room categories
 */
List *category_manager_get_by_parent_id(int category_id) {
    List *sub_categories;
    list_new(&sub_categories);

    ListIter iter;

    list_iter_init(&iter, global.room_category_manager.categories);
    room_category *category;

    while (list_iter_next(&iter, (void*) &category) != CC_ITER_END) {
        if (category->parent_id == category_id) {
            list_add(sub_categories, category);
        }
    }

    return sub_categories;
}

/**
 * Get rooms by the category id.
 * 
 * @param category_id the category id
 * @return the list of rooms
 */
List *category_manager_get_rooms(int category_id) {
    List *rooms;
    list_new(&rooms);

    HashTableIter iter;
    hashtable_iter_init(&iter, global.room_manager.rooms);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        room *instance = entry->value;

        if (instance->room_data->category == category_id) {
            list_add(rooms, instance);
        }
    }

    return rooms;
}

int category_manager_get_current_vistors(int category_id) {
    int current_visitors = 0;

    HashTableIter iter;
    hashtable_iter_init(&iter, global.room_manager.rooms);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        room *instance = entry->value;

        if (instance->room_data->category == category_id) {
            current_visitors += instance->room_data->visitors_now;

            /**
             * Recursive lisiting for child categories underneath this category
             */
            List *categories = category_manager_get_by_parent_id(category_id);
            ListIter list_iter;
            list_iter_init(&list_iter, categories);
            room_category *category;
            while (list_iter_next(&list_iter, (void*) &category) != CC_ITER_END) {
                current_visitors += category_manager_get_current_vistors(category->id);
            }
            list_destroy(categories);
        }
    }

    return current_visitors;
}

int category_manager_get_max_vistors(int category_id) {
    int max_visitors = 0;

    HashTableIter iter;
    hashtable_iter_init(&iter, global.room_manager.rooms);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        room *instance = entry->value;

        if (instance->room_data->category == category_id) {
            max_visitors += instance->room_data->visitors_max;

            /**
             * Recursive lisiting for child categories underneath this category
             */
            List *categories = category_manager_get_by_parent_id(category_id);
            ListIter list_iter;
            list_iter_init(&list_iter, categories);
            room_category *category;
            while (list_iter_next(&list_iter, (void*) &category) != CC_ITER_END) {
                max_visitors += category_manager_get_max_vistors(category->id);
            }
            list_destroy(categories);
        }
    }

    return max_visitors;
}

/**
 * Serialise rooms on the navigator.
 * 
 * @param navigator the outgoing message
 * @param instance the room instance
 * @param category_type the category type
 */
void category_manager_serialise(outgoing_message *navigator, room *instance, room_category_type category_type) {
    if (category_type == PUBLIC) {
        om_write_int(navigator, instance->room_data->id); // room id
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
}