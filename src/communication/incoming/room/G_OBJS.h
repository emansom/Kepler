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

        if (!room_item->definition->behaviour->isPublicSpaceObject) {
            continue;
        }

        sb_add_string(om->sb, room_item->custom_data);
        sb_add_string(om->sb, " ");
        sb_add_string(om->sb, room_item->definition->sprite);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, room_item->coords->x);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, room_item->coords->y);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, (int)room_item->coords->z);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, room_item->coords->rotation);
        sb_add_string(om->sb, " ");

        if (room_item->definition->behaviour->has_extra_parameter) {
            sb_add_int(om->sb, 2);
            sb_add_string(om->sb, " ");
        }

        sb_add_char(om->sb, 13);
    }
    player_send(player, om);
    om_cleanup(om);

    om = om_create(32); // "@`"
    om_write_int(om, (int)list_size(room->items));
    for (size_t i = 0; i < list_size(room->items); i++) {
        item *room_item;
        list_get_at(room->items, i, (void*)&room_item);

        if (room_item->definition->behaviour->isPublicSpaceObject) {
            continue;
        }

        om_write_int_delimeter(om, room_item->id, 2);
        om_write_str(om, room_item->definition->sprite);
        om_write_int(om, room_item->coords->x);
        om_write_int(om, room_item->coords->y);
        om_write_int(om, 1);
        om_write_int(om, 1);
        om_write_int(om, room_item->coords->rotation);
        sb_add_float(om->sb, room_item->coords->z);
        sb_add_char(om->sb, 2);
        om_write_str(om, "0");
        om_write_str(om, "");
        om_write_int(om, 0);
        om_write_str(om, room_item->custom_data);
    }
    player_send(player, om);
    om_cleanup(om);
}
