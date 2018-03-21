#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "list.h"
#include "game/items/item.h"

#include "util/stringbuilder.h"

void G_OBJS(player *player, incoming_message *message) {
    if (player->room_user->room == NULL) {
        return;
    }

    room *room = player->room_user->room;
    outgoing_message *om = om_create(30); // "@^"

    for (size_t i = 0; i < list_size(room->public_items); i++) {
        item *room_item;
        list_get_at(room->public_items, i, (void*)&room_item);

        sb_add_string(om->sb, room_item->custom_data);
        sb_add_string(om->sb, " ");
        sb_add_string(om->sb, room_item->class_name);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, room_item->x);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, room_item->y);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, (int)room_item->z);
        sb_add_string(om->sb, " ");
        sb_add_int(om->sb, room_item->rotation);
        sb_add_string(om->sb, " ");

        if (room_item->is_table) {
            sb_add_int(om->sb, 2);
            sb_add_string(om->sb, " ");
        }

        sb_add_char(om->sb, 13);
    }

    player_send(player, om);
    om_cleanup(om);

    om = om_create(32); // "@`" 
    om_write_int(om, 0);
    player_send(player, om);
    om_cleanup(om);
}
