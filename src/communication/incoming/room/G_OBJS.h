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
    om_write_int(om, (int)list_size(room->items));

    for (size_t i = 0; i < list_size(room->items); i++) {
        item *room_item;
        list_get_at(room->items, i, (void *) &room_item);

        om_write_int_delimeter(om, room_item->id, 2);
        om_write_str(om, room_item->class_name);

        om_write_int(om, room_item->x);
        om_write_int(om, room_item->y);
        om_write_int(om, 1);
        om_write_int(om, 1);
        om_write_int(om, room_item->rotation);
        sb_add_float(om->sb, room_item->z);
        sb_add_char(om->sb, 2);

        om_write_str(om, "0");
        om_write_str(om, "");

        om_write_int(om, 0);
        om_write_str(om, room_item->custom_data);
    }

    player_send(player, om);
    om_cleanup(om);

    /*if (strcmp(player->room_user->room->room_data->model_data->model_name, "pool_b") == 0) {
        char *packet = strdup("@`RB10$queue_tile2$QEQBIIH7.0$0$$I2$3$queue_tile2$QESAIIJ7.0$0$$I2$5$queue_tile2$SESAIIJ7.0$0$$I2$8$queue_tile2$RFSAIIH7.0$0$$I2$7$queue_tile2$QFSAIIJ7.0$0$$I2$4$queue_tile2$RESAIIJ7.0$0$$I2$1$queue_tile2$RFQAIIH7.0$0$$I2$9$queue_tile2$QEPBIIH7.0$0$$I2$2$queue_tile2$RFRAIIH7.0$0$$I2$6$queue_tile2$PFSAIIJ7.0$0$$I2$\1");
        for (int i = 0; i < strlen(packet); i++) {
            if (packet[i] == '$') {
                packet[i] = '\2';
            }
        }

        player_send_raw(player, packet);
        free(packet);
    }*/
}
