#include <stdlib.h>

#include "list.h"

#include "game/player/player.h"
#include "game/inventory/inventory.h"

#include "database/queries/item_query.h"

inventory *inventory_create() {
    inventory *inv = malloc(sizeof(inventory));
    inv->items = NULL;
    return inv;
}

void inventory_init(player *player) {
    if (player->inventory->items != NULL) {
        list_destroy(player->inventory->items);
    }

    player->inventory->items = item_query_get_inventory(player->player_data->id);
}

void inventory_dispose(inventory *inventory) {
    if (inventory->items != NULL) {
        list_destroy(inventory->items);
    }

    free(inventory);
}