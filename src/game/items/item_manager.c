#include "shared.h"

#include "hashtable.h"

#include "database/queries/furniture_query.h"
#include "item_manager.h"

/**
 * Load item definitions.
 */
void item_manager_init() {
    global.item_manager.definitions = furniture_query_definitions();
}

/**
 * Get item definition by id
 *
 * @param room_id the definition id
 * @return the item definition
 */
item_definition *item_manager_get_definition_by_id(int definition_id) {
    item_definition *definition = NULL;

    if (hashtable_contains_key(global.item_manager.definitions, &definition_id)) {
        hashtable_get(global.item_manager.definitions, &definition_id, (void *)&definition);
    }

    return definition;
}