#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "shared.h"

#include "communication/messages/outgoing_message.h"
#include "game/player/player.h"

#include "catalogue_item.h"

catalogue_item *catalogue_item_create(char *sale_code, int page_id, int order_id, int price, int definition_id, int item_specialspriteid, char *package_name, char *package_description, bool is_package) {
    catalogue_item *item = malloc(sizeof(catalogue_item));
    item->sale_code = strdup(sale_code);
    item->page_id = page_id;
    item->order_id = order_id;
    item->price = price;
    item->definition_id = definition_id;
    item->item_specialspriteid = item_specialspriteid;
    item->package_name = package_name;
    item->package_description = package_description;
    item->is_package = is_package;
    return item;
}