#ifndef MESSENGER_H
#define MESSENGER_H

typedef struct list_s List;
typedef struct player_s player;

typedef struct messenger_s {
    List *friends;
} messenger;

messenger *messenger_create();
void messenger_init(player*);
void messenger_cleanup(messenger*);

#endif