#ifndef PLAYER_MANAGER_H
#define PLAYER_MANAGER_H

typedef struct hashset_s HashSet;

struct player_manager {
    HashSet *players;
};

void player_manager_init();
void player_manager_print();

#endif