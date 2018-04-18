#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/item_query.h"

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

    if (item->is_package) {
        goto cleanup; // TODO: Purchase packages
    }


    if (item->price > player->player_data->credits) {
        outgoing_message *om = om_create(68); // "AD"
        player_send(player, om);
        om_cleanup(om);

        goto cleanup;
    }

    char *custom_data = NULL;

    if (!item->is_package) {
        char *extra_data = get_argument(content, "\r", 4);


        if (item->definition->behaviour->is_decoration) {
            if (is_numeric(extra_data)) {
                custom_data = strdup(extra_data);
            }
        } else {
            if (item->item_specialspriteid > 0) {
                char num[10];
                sprintf(num, "%i", item->item_specialspriteid);
                custom_data = strdup(num);
            }
        }

        if (item->definition->behaviour->is_prize_trophy) {
            filter_vulnerable_characters(&extra_data, true);

            char *short_date = get_short_time_formatted();

            stringbuilder *sb = sb_create();
            sb_add_string(sb, player->player_data->username);
            sb_add_char(sb, 9);
            sb_add_string(sb, short_date);
            sb_add_char(sb, 9);
            sb_add_string(sb, extra_data);

            custom_data = strdup(sb->data);

            sb_cleanup(sb);
            free(short_date);
        }

        free(extra_data);
    }

    player->player_data->credits -= item->price;
    player_send_credits(player);

    item_query_create(player->player_data->id, 0, item->definition->id, 0, 0, 0, 0, custom_data);
    player_query_save_currency(player);

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