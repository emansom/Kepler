#ifndef INVENTORY_H
#define INVENTORY_H

typedef struct list_s List;
typedef struct player_s player;

typedef struct inventory_s {
    List *items;
    int hand_strip_page_index;
} inventory;

inventory *inventory_create();
void inventory_init(player *player);
void inventory_change_view(inventory *inventory, char *strip_view);
char *inventory_get_casts(inventory*, int *count);
void inventory_dispose(inventory *inventory);

#endif