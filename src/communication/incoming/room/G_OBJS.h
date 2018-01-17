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
    outgoing_message *om;

    if (strcmp(room->room_data->model_data->model_id, "ice_cafe") == 0) {
        player_send_raw(player, "@^711 cafe_chair_cream 17 17 0 6 \r666 infokiosk 17 0 0 4 \r692 cafe_deskb 1 10 1 0 \r708 cafe_chair_blue 5 17 1 0 \r672 cafe_table_biga_blue 4 2 1 2 2 \r664 infokiosk 15 0 0 4 \r667 cafe_chair_blue 3 1 1 2 \r718 cafe_sofaa 2 21 1 2 \r698 cafe_deskb 1 12 1 0 \r722 cafe_sofaa 2 23 1 2 \r681 cafe_chair_cream 13 3 0 6 \r683 cafe_chair_cream 10 4 0 2 \r695 cafe_table_cream 15 10 0 2 2 \r684 cafe_chair_cream 13 4 0 6 \r704 cafe_chair_blue 3 15 1 2 \r700 cafe_deskb 1 13 1 0 \r716 cafe_sofab 2 20 1 2 \r721 construction 8 22 0 0 \r671 cafe_chair_blue 3 2 1 2 \r694 cafe_chair_cream 14 10 0 2 \r685 cafe_chair_cream 12 5 0 0 \r709 cafe_chair_cream 14 17 0 2 \r693 cafe_chair_blue 5 10 1 0 \r702 cafe_chair_blue 5 14 1 4 \r691 cafe_chair_cream 15 9 0 4 \r675 cafe_table_bigb_cream 12 2 0 0 \r677 cafe_chair_blue 3 3 1 2 \r679 cafe_chair_cream 10 3 0 2 \r680 cafe_table_biga_cream 11 3 0 2 2 \r713 cafe_chair_cream 10 19 0 2 \r697 cafe_chair_cream 17 11 0 6 \r682 cafe_chair_blue 5 4 1 0 \r669 cafe_chair_blue 6 1 1 6 \r706 cafe_chair_blue 6 16 1 6 \r687 cafe_chair_blue 3 8 1 2 \r719 cafe_chair_cream 11 21 0 0 \r668 cafe_table_bigb_blue 5 1 1 0 \r712 cafe_chair_cream 11 18 0 4 \r688 cafe_table_blue 4 8 1 2 2 \r710 cafe_table_cream 15 17 0 2 2 \r674 cafe_chair_cream 10 2 0 2 \r665 infokiosk 16 0 0 4 \r720 cafe_sofab 2 22 1 2 \r717 cafe_chair_cream 13 20 0 6 \r707 cafe_chair_cream 15 16 0 4 \r715 cafe_chair_cream 16 19 0 0 \r689 cafe_deskc 1 9 1 0 \r690 cafe_chair_blue 6 9 1 6 \r699 cafe_chair_cream 15 12 0 0 \r670 cafe_chair_cream 11 1 0 4 \r673 cafe_chair_blue 6 2 1 6 \r705 cafe_table_blue 4 15 1 2 2 \r678 cafe_chair_blue 6 3 1 6 \r696 cafe_deskb 1 11 1 0 \r714 cafe_table_cream 11 19 0 2 2 \r701 cafe_deskb 1 14 1 0 \r676 cafe_chair_cream 13 2 0 6 \r686 cafe_chair_blue 4 7 1 4 \r\1");

    } else {
        om = om_create(30); // "@^"

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
            sb_add_char(om->sb, 13);
        }

        player_send(player, om);
        om_cleanup(om);
    }

    om = om_create(32); // "@`" 
    om_write_int(om, 0);
    player_send(player, om);
    om_cleanup(om);
}
