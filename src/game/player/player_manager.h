#ifndef PLAYER_MANAGER_H
#define PLAYER_MANAGER_H

typedef struct hashtable_s HashTable;
typedef struct player_s player;

struct player_manager {
    HashTable *players;
};

void player_manager_init();
player *player_manager_add(void*, char *ip);
void player_manager_remove(void*);
player *player_manager_find(void*);

#endif