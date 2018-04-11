#ifndef ITEM_MANAGER_H
#define ITEM_MANAGER_H

typedef struct hashtable_s HashTable;
typedef struct item_definition_s item_definition;

struct item_manager {
    HashTable *definitions;
};

void item_manager_init();

#endif