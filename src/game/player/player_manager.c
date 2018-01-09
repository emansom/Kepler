#include "player_manager.h"
#include "shared.h"

#include "lib/collections/list.h"
#include "lib/dyad/dyad.h"

void player_manager_init() {
    list_new(&global.player_manager.players);
    /*int size = 10000;
    int i;
    for (i = 0; i < size; i++) {
        int *e = malloc(sizeof(int));
        *e = 1;//rand() % 1000001;
        list_add(global.player_manager.players, e);
    }*/
}

void player_manager_add(dyad_Stream *stream) {
    list_add(global.player_manager.players, stream);
}

void player_manager_remove(dyad_Stream *stream) {
    list_remove_last(global.player_manager.players, (void*) stream);
}

void player_manager_print() {
    ListIter iter;
    list_iter_init(&iter, global.player_manager.players);

    dyad_Stream *e;
    while (list_iter_next(&iter, (void*) &e) != CC_ITER_END) {
        printf("testing!\n");
    }
}

/*void player_manager_print() {
    ListIter iter;
    list_iter_init(&iter, global.player_manager.players);

    void *prev;
    void *e;
    list_iter_next(&iter, &prev);
    while (list_iter_next(&iter, &e) != CC_ITER_END) {
        int *i = (int*)e;
        printf("Hello %i\n", *i);
    }
}*/