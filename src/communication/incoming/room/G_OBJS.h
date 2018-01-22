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

    ListIter iter;
    list_iter_init(&iter, room->public_items);

    item *room_item;
    while (list_iter_next(&iter, (void*) &room_item) != CC_ITER_END) {
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
