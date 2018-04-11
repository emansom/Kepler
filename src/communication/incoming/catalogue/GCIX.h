#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "hashtable.h"

#include "game/catalogue/catalogue_manager.h"
#include "game/catalogue/catalogue_page.h"

#include "game/player/player.h"

void GCIX(player *player, incoming_message *message) {
    HashTable *pages = catalogue_manager_get_pages();

    HashTableIter iter;
    hashtable_iter_init(&iter, global.catalogue_manager.pages);
    TableEntry *entry;

    outgoing_message *catalogue_pages = om_create(126); // "A~"
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        catalogue_page *page = entry->value;

        if (player->player_data->rank >= page->min_role) {
            om_write_str_delimeter(catalogue_pages, page->name_index, 9);
            om_write_str_delimeter(catalogue_pages, page->name, 13);
        }
    }

    player_send(player, catalogue_pages);
    om_cleanup(catalogue_pages);
}
