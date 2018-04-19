#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "list.h"

#include "game/items/item.h"
#include "game/pathfinder/coord.h"
#include "game/player/player.h"

#include "util/stringbuilder.h"

void G_OBJS(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    room *room = player->room_user->room;

    outgoing_message *om = om_create(30); // "@^

    for (size_t i = 0; i < list_size(room->items); i++) {
        item *room_item;
        list_get_at(room->items, i, (void*)&room_item);

        if (!room_item->definition->behaviour->is_public_space_object) {
            continue;
        }

        char *item_string = item_as_string(room_item);
        sb_add_string(om->sb, item_string);
        free(item_string);
    }

    player_send(player, om);
    om_cleanup(om);

    om = om_create(32); // "@`"
    om_write_int(om, (int)list_size(room->items));

    for (size_t i = 0; i < list_size(room->items); i++) {
        item *item;
        list_get_at(room->items, i, (void*)&item);

        if (item->definition->behaviour->is_public_space_object) {
            continue;
        }

        if (item->definition->behaviour->is_wall_item) {
            continue;
        }

        char *item_string = item_as_string(item);
        sb_add_string(om->sb, item_string);
        free(item_string);
    }

    player_send(player, om);
    om_cleanup(om);
}
