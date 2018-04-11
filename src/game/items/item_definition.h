#ifndef ITEM_DEFINITION_H
#define ITEM_DEFINITION_H

typedef struct item_definition_s {
    int id;
} item_definition;

item_definition *item_definition_create(int);

#endif