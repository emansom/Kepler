#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/inventory/inventory.h"

// Credits to Nillus from Woodpecker v3, very helpful code!
void GETSTRIP(player *player, incoming_message *im) {
    char *strip_view = im_get_content(im);

    inventory *inv = (inventory *) player->inventory;
    inventory_change_view(inv, strip_view);

    int total_count = 0;
    char *item_casts = inventory_get_casts(inv, &total_count);

    outgoing_message *om = om_create(140); // "BL"
    sb_add_string(om->sb, item_casts);
    sb_add_char(om->sb, 13);
    sb_add_int(om->sb, total_count);
    player_send(player, om);
    om_cleanup(om);

    if (strip_view != NULL) {
        free(strip_view);
    }
}
