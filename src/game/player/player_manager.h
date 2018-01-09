#ifndef PLAYER_MANAGER_H
#define PLAYER_MANAGER_H

typedef struct list_s List;
typedef struct dyad_Stream dyad_Stream;

struct player_manager {
    List *players;
};

void player_manager_init();
void player_manager_add(dyad_Stream*);
void player_manager_remove(dyad_Stream*);
void player_manager_print();

#endif