#ifndef POOL_HANDLER_H
#define POOL_HANDLER_H

typedef struct player_s player;
typedef struct item_s item;
typedef struct room_s room;

void pool_booth_exit(player*);
void pool_booth_walk_on(player*, item*);
void pool_setup_redirections(room*, item*);

#endif