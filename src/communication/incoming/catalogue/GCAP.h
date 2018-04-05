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

    printf("page name: %s\n", page_name);

    outgoing_message *catalogue_page = om_create(127); // "A"
    player_send(player, catalogue_page);
    om_cleanup(catalogue_page);

    cleanup:
        free(content);
        free(page_name);
}
