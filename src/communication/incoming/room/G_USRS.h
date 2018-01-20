#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"

void G_USRS(player *p, incoming_message *message) {
    if (p->room_user->room == NULL) {
        return;
    }

    if (p->room_user->room->room_data->model_data == NULL) {
        printf("Room %i has invalid model data.\n", p->room_user->room->room_data->id);
        return;
    }

    room *room = p->room_user->room;

    ListIter iter;
    list_iter_init(&iter, room->users);
    player *user;

    outgoing_message *players = om_create(28); // "@\"
    while (list_iter_next(&iter, (void*) &user) != CC_ITER_END) {
        append_user_list(players, user);
    }
    player_send(p, players);
    om_cleanup(players);

    players = om_create(28); // "@\"
    append_user_list(players, p);
    room_send(room, players);


    /*char instance_id[11];
    sprintf(instance_id, "%i", player->player_data->id);
    instance_id[10] = '\0';

    outgoing_message *players = om_create(28); // "@\"
    om_write_str_kv(players, "i", "0");
    om_write_str_kv(players, "a", instance_id);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    sb_add_string(players->sb, "l");
    sb_add_string(players->sb, ":");
    sb_add_int(players->sb, player->room_user->room->room_data->model_data->door_x);
    sb_add_int(players->sb, player->room_user->room->room_data->model_data->door_y);
    sb_add_float(players->sb, player->room_user->room->room_data->model_data->door_z);
    sb_add_char(players->sb, 13);
    om_write_str_kv(players, "c", player->player_data->motto);
    player_send(player, players);
    om_cleanup(players);*/
}