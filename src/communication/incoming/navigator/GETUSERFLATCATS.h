#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "game/navigator/navigator_category_manager.h"

#include "list.h"

void GETUSERFLATCATS(player *player, incoming_message *message) {
    outgoing_message *navigator = om_create(221); // "C]"

    List *categories = category_manager_flat_categories();
    om_write_int(navigator, list_size(categories)); // category count

    for (int i = 0; i < list_size(categories); i++) {
        room_category *category;
        list_get_at(categories, i, (void*)&category);

        om_write_int(navigator, category->id); // category id
        om_write_str(navigator, category->name); // category name
    }

    player_send(player, navigator);
    om_cleanup(navigator);
    list_destroy(categories);
}
