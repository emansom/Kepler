#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "list.h"

#include "game/catalogue/catalogue_manager.h"
#include "game/catalogue/catalogue_page.h"

#include "game/player/player.h"

void GCAP(player *player, incoming_message *message) {
    char *content = im_get_content(message);
    char *page_name = get_argument(content, "/", 1);

    catalogue_page *page = catalogue_manager_get_page_by_index(page_name);

    if (page == NULL) {
        goto cleanup;
    }

    outgoing_message *catalogue_page = om_create(127); // "A"
    om_write_str_kv(catalogue_page, "i", page->name_index);
    om_write_str_kv(catalogue_page, "n", page->name);
    om_write_str_kv(catalogue_page, "l", page->layout);
    om_write_str_kv(catalogue_page, "g", page->image_headline);
    om_write_str_kv(catalogue_page, "e", page->image_teasers);
    om_write_str_kv(catalogue_page, "h", page->body);

    if (page->label_pick != NULL) {
        om_write_str_kv(catalogue_page, "w", page->label_pick);
    }

    if (page->label_extra_s != NULL) {
        om_write_str_kv(catalogue_page, "s", page->label_extra_s);
    }

    player_send(player, catalogue_page);
    om_cleanup(catalogue_page);

    cleanup:
        free(content);
        free(page_name);
}
