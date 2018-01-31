#ifndef PLAYER_MANAGER_H
#define PLAYER_MANAGER_H

typedef struct list_s List;
typedef struct player_s player;

struct player_manager {
    List *players;
};

void player_manager_init();
player *player_manager_add(void*, char *ip);
void player_manager_remove(player*);
player *player_manager_find(void*);
player *player_manager_find_by_id(int);

#endif