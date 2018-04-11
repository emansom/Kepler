#include "shared.h"

#include "database/queries/furniture_query.h"
#include "item_manager.h"

/**
 * Load item definitions.
 */
void item_manager_init() {
    global.item_manager.definitions = furniture_query_definitions();
    global.item_manager.sprite_index = om_create(295); "Dg";
    om_write_int(global.item_manager.sprite_index, (int)hashtable_size(global.item_manager.definitions));

    HashTableIter iter;
    TableEntry *entry;

    hashtable_iter_init(&iter, global.item_manager.definitions);

    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        item_definition *def = entry->value;

        om_write_str(global.item_manager.sprite_index, def->sprite);
        om_write_int(global.item_manager.sprite_index, def->cast_directory);
    }
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