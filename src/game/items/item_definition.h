#ifndef ITEM_DEFINITION_H
#define ITEM_DEFINITION_H

#include "game/items/item_behaviour.h"

typedef struct item_definition_s {
    int id;
    int cast_directory;
    char *sprite;
    char *colour;
    int length;
    int width;
    double top_height;
    char *behaviour_data;
    item_behaviour *behaviour;
} item_definition;

item_definition *item_definition_create(int id, int cast_directory, char *sprite, char *colour, int length, int width, double top_height, char *behaviour);
char *item_definition_get_name(item_definition *definition, int special_sprite_id);
char *item_definition_get_desc(item_definition *definition, int special_sprite_id);
char *item_definition_get_icon(item_definition *definition, int special_sprite_id);
char *item_definition_get_text_key(item_definition *definition, int special_sprite_id);

#endif