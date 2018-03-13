#ifndef MESSENGER_H
#define MESSENGER_H

typedef struct hashtable_s HashTable;
typedef struct player_s player;

typedef struct messenger_s {
    HashTable *friends;
} messenger;

messenger *messenger_create();
void messenger_init(player*);
void messenger_cleanup(messenger*);

#endif