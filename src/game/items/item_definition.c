#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "item_definition.h"

item_definition *item_definition_create(int id) {
    item_definition *def = malloc(sizeof(item_definition));
    def->id = id;
    return def;
}