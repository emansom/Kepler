#include <stdlib.h>
#include <string.h>

#include "communication/messages/outgoing_message.h"
#include "game/player/player.h"

#include "catalogue_page.h"

catalogue_page *catalogue_page_create() {
    catalogue_page *category = malloc(sizeof(catalogue_page));
    return category;
}