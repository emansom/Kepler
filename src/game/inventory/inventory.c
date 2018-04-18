#include <stdlib.h>
#include <game/player/player.h>

#include "list.h"

#include "game/inventory/inventory.h"

inventory *inventory_create() {
    inventory *inv = malloc(sizeof(inventory));
    inv->items = NULL;
    return inv;
}

void inventory_init(player *player) {
    if (player->inventory->items != NULL) {
        list_destroy(player->inventory->items);
    } else {
        list_new(&player->inventory->items);
    }


}

void inventory_dispose(inventory *inventory) {

}