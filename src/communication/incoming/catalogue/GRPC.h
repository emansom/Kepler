#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void GRPC(player *player, incoming_message *message) {
    char *content = im_get_content(message);

    char *page = get_argument(content, "\r", 1);
    char *sale_code = get_argument(content, "\r", 3);

    if (page == NULL) {
        goto cleanup;
    }

    if (sale_code == NULL) {
        goto cleanup;
    }

    catalogue_page *store_page = (catalogue_page *) catalogue_manager_get_page_by_index(page);

    if (store_page == NULL) {
        goto cleanup;
    }

    if (player->player_data->rank < store_page->min_role) {
        goto cleanup;
    }

    catalogue_item *item = (catalogue_item *) catalogue_manager_get_item(sale_code);

    if (item == NULL) {
        goto cleanup;
    }


    if (item->price > player->player_data->credits) {
        outgoing_message *om = om_create(68); // "AD"
        player_send(player, om);
        om_cleanup(om);

        goto cleanup;
    }

    

    cleanup:
        if (content != NULL) {
            free(content);
        }

        if (page != NULL) {
            free(page);
        }

        if (sale_code != NULL) {
            free(sale_code);
        }
}