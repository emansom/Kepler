#include "player_manager.h"
#include "shared.h"

#include "lib/collections/hashset.h"

void player_manager_init() {
    hashset_new(&global.player_manager.players);

    char *a = "foo";
    char *b = "bar";
    char *c = "baz";
    char *d = "foo";

    hashset_add(global.player_manager.players, a);
    hashset_add(global.player_manager.players, b);
    hashset_add(global.player_manager.players, c);
    hashset_add(global.player_manager.players, d);
}

void player_manager_print() {

    HashSetIter iter;
    hashset_iter_init(&iter, global.player_manager.players);

    char *e;
    while (hashset_iter_next(&iter, (void*)&e) != CC_ITER_END) {
        printf("Hello %s\n", e);
    }
}