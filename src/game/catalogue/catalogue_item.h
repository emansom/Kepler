#ifndef CATALOGUE_ITEM_H
#define CATALOGUE_ITEM_H

#include <stdbool.h>

typedef struct catalogue_item_s {
    char *sale_code;
    int page_id;
    int order_id;
    int price;
    int definition_id;
    int item_specialspriteid;
    char *package_name;
    char *package_description;
    bool is_package;
} catalogue_item;

catalogue_item *catalogue_item_create(char *sale_code, int page_id, int order_id, int price, int definition_id, int item_specialspriteid, char *package_name, char *package_description, bool is_package);

#endif