#ifndef INVENTORY_H
#define INVENTORY_H

typedef struct list_s List;
typedef struct player_s player;

typedef struct inventory_s {
    List *items;
} inventory;

inventory *inventory_create();
void inventory_init(player *player);
void inventory_dispose(inventory *inventory);

#endif