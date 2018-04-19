#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/inventory/inventory.h"
#include "game/player/player.h"
#include "game/room/room.h"
#include "game/items/item.h"

void PLACESTUFF(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    if (!room_has_rights(player->room_user->room, player->player_data->id)) {
        return;
    }

    char *content = im_get_content(message);

    if (content == NULL) {
        return;
    }

    inventory *inv = (inventory *) player->inventory;

    char *str_id = get_argument(content, " ", 0);

    char *str_x = NULL;
    char *str_y = NULL;

    if (str_id == NULL) {
        goto cleanup;
    }

    item *item = inventory_get_item(inv, (int)strtol(str_id, NULL, 10));

    if (item == NULL) {
        goto cleanup;
    }

    if (item->definition->behaviour->is_wall_item) {
        printf("Is wall item\n");
    } else {
        str_x = get_argument(content, " ", 1);
        str_y = get_argument(content, " ", 2);

        if (str_x == NULL || str_y == NULL) {
            goto cleanup;
        }

        item->coords->x = (int) strtol(str_x, NULL, 10);
        item->coords->y = (int) strtol(str_y, NULL, 10);
        item->coords->rotation = 0;
    }


    room_map_add_item(player->room_user->room, item);

    cleanup:
        free(content);

        if (str_id != NULL) {
            free(str_id);
        }

        if (str_x != NULL) {
            free(str_x);
        }
}
