#include "shared.h"

#include "hashtable.h"

#include "database/queries/furniture_query.h"
#include "item_manager.h"

void item_manager_init() {
    global.item_manager.definitions = furniture_query_definitions();
}