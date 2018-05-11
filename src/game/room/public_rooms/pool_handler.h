#ifndef POOL_HANDLER_H
#define POOL_HANDLER_H

typedef struct session_s session;
typedef struct item_s item;
typedef struct room_s room;

void pool_booth_exit(session*);
void pool_item_walk_on(session *, item *);
void pool_setup_redirections(room*, item*);

#endif