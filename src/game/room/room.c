#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "list.h"
#include "util/stringbuilder.h"

#include "room.h"
#include "room_model.h"
#include "room_user.h"

#include "game/player/player.h"
#include "database/queries/player_query.h"
#include "communication/messages/outgoing_message.h"

room *room_create(int room_id) {
    room *instance = malloc(sizeof(room));
    instance->room_id = room_id;
    instance->room_data = NULL;
    list_new(&instance->users);
    return instance;
}

room_data *room_create_data(int id, int owner_id, int category, char *name, char *description, char *model, char *ccts, int wallpaper, int floor, int showname, int superusers, int accesstype, char *password, int visitors_now, int visitors_max) {
    room_data *data = malloc(sizeof(room_data));
    data->id = id;
    data->owner_id = owner_id;
    data->owner_name = query_player_username(owner_id);
    data->category = category;
    data->name = strdup(name);
    data->description = strdup(description);
    data->model = strdup(model);
    data->model_data = model_manager_get(model);
    if (ccts == NULL) {
        data->ccts = strdup("");
    } else {
        data->ccts = strdup(ccts);
    }
    data->wall = wallpaper;
    data->floor = floor;
    data->show_name = showname;
    data->superusers = superusers;
    data->accesstype = accesstype;
    data->password = strdup(password);
    data->visitors_now = visitors_now;
    data->visitors_max = visitors_max;
    return data;
}

void room_enter(room *room, player *player) {
    if (list_contains(room->users, player)) {
        return;
    }

    player->room_user->room = room;
    player->room_user->room_id = room->room_id;

    list_add(room->users, player);
}

void append_user_list(outgoing_message *players, player *player) {
    char instance_id[11];
    sprintf(instance_id, "%i", player->player_data->id);
    instance_id[10] = '\0';

    om_write_str_kv(players, "i", "0");
    om_write_str_kv(players, "a", instance_id);
    om_write_str_kv(players, "n", player->player_data->username);
    om_write_str_kv(players, "f", player->player_data->figure);
    sb_add_string(players->sb, "l");
    sb_add_string(players->sb, ":");
    sb_add_int(players->sb, player->room_user->room->room_data->model_data->door_x);
    sb_add_string(players->sb, " ");
    sb_add_int(players->sb, player->room_user->room->room_data->model_data->door_y);
    sb_add_string(players->sb, " ");
    sb_add_float(players->sb, player->room_user->room->room_data->model_data->door_z);
    sb_add_char(players->sb, 13);
    om_write_str_kv(players, "c", player->player_data->motto);
}

void room_send(room *room, outgoing_message *message) {
    ListIter iter;
    list_iter_init(&iter, room->users);

    player *player;
    while (list_iter_next(&iter, (void*) &player) != CC_ITER_END) {
        player_send(player, message);
    }
}

void room_cleanup(room *room) {
    if (room->room_data != NULL) {
        free(room->room_data->name);
        free(room->room_data->owner_name);
        free(room->room_data->description);
        free(room->room_data->model);
        free(room->room_data->ccts);
        free(room->room_data->password);
        free(room->room_data);
    }

    free(room);
}