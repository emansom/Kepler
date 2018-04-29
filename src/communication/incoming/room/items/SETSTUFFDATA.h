#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/inventory/inventory.h"
#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/mapping/room_map.h"

#include "game/items/item.h"

void SETSTUFFDATA(session *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    char *str_item_id = im_read_str(message);
    char *str_data = im_read_str(message);

    if (str_item_id == NULL || str_data == NULL) {
        goto cleanup;
    }

    int item_id = (int) strtol(str_item_id, NULL, 10);
    item *item = room_item_manager_get(player->room_user->room, item_id);

    if (item == NULL || !item_contains_custom_data(item->definition)) {
        goto cleanup;
    }

    if (item->definition->behaviour->requires_rights_for_interaction &&
        !room_has_rights(player->room_user->room, player->player_data->id)) {
        goto cleanup;
    }

    char *new_data = NULL;

    if (!item->definition->behaviour->is_door) {
        if (item->definition->behaviour->custom_data_true_false && (strcmp(str_data, "TRUE") == 0 || strcmp(str_data, "FALSE") == 0)) {
            new_data = strdup(str_data);
        }

        if (item->definition->behaviour->custom_data_numeric_on_off && (strcmp(str_data, "2") == 0 || strcmp(str_data, "1") == 0)) {
            new_data = strdup(str_data);
        }

        if (item->definition->behaviour->custom_data_on_off && (strcmp(str_data, "ON") == 0 || strcmp(str_data, "OFF") == 0)) {
            new_data = strdup(str_data);
        }
    } else {

    }

    if (new_data != NULL) {
        if (item->custom_data != NULL) {
            free(item->custom_data);
            item->custom_data = NULL;
        }

        item->custom_data = new_data;

        outgoing_message *om = om_create(88);
        sb_add_int_delimeter(om->sb, item->id, 2);
        om_write_str(om, item->custom_data);
        om_write_str(om, "");
        room_send(player->room_user->room, om);

        if (!item->definition->behaviour->custom_data_true_false) {
            item_query_save(item);
        }
    }

    cleanup:
    free(str_item_id);
    free(str_data);

}
