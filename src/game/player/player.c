#include <stdio.h>
#include <stdbool.h>

#include "uv.h"
#include "list.h"
#include "log.h"
#include "shared.h"

#include "game/inventory/inventory.h"
#include "game/messenger/messenger.h"

#include "game/player/player.h"
#include "game/player/player_manager.h"

#include "game/room/room.h"
#include "game/room/room_user.h"
#include "game/room/room_manager.h"
#include "game/room/manager/room_entity_manager.h"

#include "util/stringbuilder.h"
#include "util/configuration/configuration.h"

#include "communication/messages/outgoing_message.h"

#include "server/server_listener.h"
#include "database/queries/player_query.h"

/**
 * Creates a new player
 * @return player struct
 */
session *player_create(void *socket, char *ip_address) {
    session *player = malloc(sizeof(session));
    player->stream = socket;
    player->disconnected = false;
    player->ip_address = strdup(ip_address);
    player->player_data = NULL;
    player->logged_in = false;
    player->ping_safe = true;
    player->room_user = NULL;
    player->messenger = NULL;
    player->inventory = NULL;
    return player;
}

/**
 * Creates a new player data instance
 * @return player data struct
 */
player_data *player_create_data(int id, char *username, char *password, char *figure, char *pool_figure, int credits, char *motto, char *sex, int tickets, int film, int rank, char *console_motto, char *last_online, unsigned long club_subscribed, unsigned long club_expiration, char *active_badge) {
    player_data *data = malloc(sizeof(player_data));
    data->id = id;
    data->username = strdup(username);
    data->password = strdup(password);
    data->figure = strdup(figure);
    data->pool_figure = strdup(pool_figure);
    data->credits = credits;
    data->motto = strdup(motto);
    data->console_motto = strdup(console_motto);
    data->sex = strdup(sex);
    data->tickets = tickets;
    data->film = film;
    data->rank = rank;
    data->last_online = strtoul(last_online, NULL, 10);
    data->club_subscribed = (time_t)club_subscribed;
    data->club_expiration = (time_t)club_expiration;
    data->active_badge = strdup(active_badge);
    return data;
}

/**
 * Called when a connection is successfully opened
 *
 * @param p the player struct
 */
void player_login(session *player) {
    outgoing_message *om;

    player->room_user = room_user_create(player);
    player->messenger = messenger_create();
    player->inventory = inventory_create();

    messenger_init(player);
    inventory_init(player);

    player_query_save_last_online(player);
    room_manager_add_by_user_id(player->player_data->id);

    om = om_create(2); // @B
    om_write_str(om, "default\2fuse_login\2fuse_buy_credits\2fuse_trade\2fuse_room_queue_default\2fuse_performance_panel");
    player_send(player, om);
    om_cleanup(om);

    om = om_create(3); // @C
    player_send(player, om);
    om_cleanup(om);

    if (configuration_get_bool("welcome.message.enabled")) {
        char *welcome_template = configuration_get_string("welcome.message.content");
        char *welcome_custom = replace(welcome_template, "%username%", player->player_data->username);

        send_alert(player, welcome_custom);
        free(welcome_custom);
    }

    player->ping_safe = true;
    player->logged_in = true;
}

/**
 * Disconnect user
 *
 * @param p the player struct
 */
void player_disconnect(session *p) {
    if (p == NULL || p->disconnected) {
        return;
    }

    uv_close((uv_handle_t *) p->stream, server_on_connection_close);
}

/**
 * Send an outgoing message to the socket
 *
 * @param p the player struct
 * @param om the outgoing message
 */
void player_send(session *p, outgoing_message *om) {
    if (om == NULL || p == NULL || p->disconnected) {
        return;
    }

    if (configuration_get_bool("debug")) {
        char *preview = replace_unreadable_characters(om->sb->data);
        log_debug("Client [%s] outgoing data: %i / %s", p->ip_address, om->header_id, preview);
        free(preview);
    }

    om_finalise(om);
    uv_write_t *req;

    if(!(req = malloc(sizeof(uv_write_t)))){
        return;
    }

    size_t message_length = strlen(om->sb->data);

    uv_buf_t buffer = uv_buf_init(malloc(message_length), (unsigned int) message_length);
    memcpy(buffer.base, om->sb->data, message_length);

    req->handle = (void*) p;
    req->data = buffer.base;

    int response = uv_write(req, (uv_stream_t *) p->stream, &buffer, 1, &server_on_write);
}

/**
 * Sends the key of an error, whose description value is inside the external_texts of the client.
 *
 * @param p the player
 * @param error the error message
 */
void send_localised_error(session *p, char *error) {
    outgoing_message *om = om_create(33); // @a
    sb_add_string(om->sb, error);
    player_send(p, om);
    om_cleanup(om);
}

/**
 * Send an alert to the player
 *
 * @param p the player
 * @param text the alert message
 */
void send_alert(session *p, char *text) {
    outgoing_message *alert = om_create(139); // BK
    om_write_str(alert, text);
    player_send(p, alert);
    om_cleanup(alert);
}

/**
 * Send credit amount to player.
 *
 * @param player the player to send to
 */
void session_send_credits(session *player) {
    char credits_string[10 + 1]; ///"num + /0";
    sprintf(credits_string, "%i", player->player_data->credits);

    outgoing_message *credits = om_create(6); // "@F"
    om_write_str(credits, credits_string);
    sb_add_string(credits->sb, ".0");
    player_send(player, credits);
    om_cleanup(credits);
}

/**
 * Send ticket amount to player.
 *
 * @param player the player to send to
 */
void session_send_tickets(session *player) {
    char credits_string[10 + 1]; ///"num + /0";
    sprintf(credits_string, "%i", player->player_data->tickets);

    outgoing_message *credits = om_create(124); // "A|"
    sb_add_string(credits->sb, credits_string);
    player_send(player, credits);
    om_cleanup(credits);
}

/*
 * Refreshes user appearance
 *
 * @param player to refresh
 */
void player_refresh_appearance(session *player) {
    player_data *new_data = player_query_data(player->player_data->id);

    // Reload figure, gender and motto
    log_debug("Figure: %s, sex: %s, motto: %s", new_data->figure, new_data->sex, new_data->motto);
    player->player_data->figure = strdup(new_data->figure);
    player->player_data->sex = strdup(new_data->sex);
    player->player_data->motto = strdup(new_data->motto);

    player_data_cleanup(new_data);

    // Send refresh to user
    outgoing_message *user_info = om_create(5);
    om_write_str_int(user_info, player->player_data->id);
    om_write_str(user_info, player->player_data->username);
    om_write_str(user_info, player->player_data->figure);
    om_write_str(user_info, player->player_data->sex);
    om_write_str(user_info, player->player_data->motto);
    om_write_int(user_info, player->player_data->tickets);
    om_write_str(user_info, player->player_data->pool_figure); // pool figure
    om_write_int(user_info, player->player_data->film);
    player_send(player, user_info);
    om_cleanup(user_info);

    // Send refresh to room if inside room
    if (player->room_user != NULL && player->room_user->room != NULL) {
        outgoing_message *poof = om_create(266);
        om_write_int(poof, player->room_user->instance_id);
        om_write_str(poof, player->player_data->figure);
        om_write_str(poof, player->player_data->sex);
        om_write_str(poof, player->player_data->motto);
        room_send(player->room_user->room, poof);
        om_cleanup(poof);
    }
}


/*
 * Refreshes user badges
 *
 * @param player to refresh
 */
void player_refresh_badges(session *player) {
    Array *badges = player_query_badges(player->player_data->id);

    // if (player->player_data->club_days_left > 0) {
    //     if (array_add(badges, "HC1") != CC_OK) {
    //         log_fatal("Couldn't add HC1 to array in player_refresh_badges");
    //     }
    //
    //     // If the user has been subscribed for more than 11 months, give out the gold HC badge :)
    //     if (player->player_data->club_months_expired > 11) {
    //         if (array_add(badges, "HC2") != CC_OK) {
    //             log_fatal("Couldn't add HC2 to array in player_refresh_badges");
    //         }
    //     }
    // }

    outgoing_message *badge_list = om_create(229); // "Ce"
    om_write_int(badge_list, (int)array_size(badges));

    ArrayIter ai;
    array_iter_init(&ai, badges);

    int badge_slot = 0;
    int slot_counter = 0;
    void *next;

    while (array_iter_next(&ai, &next) != CC_ITER_END) {
        char *badge = (char*)next;
        om_write_str(badge_list, badge);

        if (badge == player->player_data->active_badge) {
            badge_slot = slot_counter;
        }

        slot_counter++;
    }

    om_write_int(badge_list, badge_slot);
    om_write_int(badge_list, strlen(player->player_data->active_badge) > 0);

    player_send(player, badge_list);
    om_cleanup(badge_list);

    array_destroy(badges);
}

/**
 * Called when a connection is closed
 * @param player the player struct
 */
void player_cleanup(session *player) {
    if (player == NULL) {
        return;
    }

    player_manager_remove(player);

    if (player->room_user != NULL) {
        if (player->room_user->room != NULL) {
            room_leave(player->room_user->room, player, false);
        }
    }

    if (player->player_data != NULL) {
        player_query_save_last_online(player);

        List *rooms = room_manager_get_by_user_id(player->player_data->id);

        for (size_t i = 0; i < list_size(rooms); i++) {
            room *room;
            list_get_at(rooms, i, (void*)&room);

            if (room != NULL) {
                room_dispose(room, false);
            }
        }

        list_destroy(rooms);
    }

    if (player->room_user != NULL) {
        room_user_cleanup(player->room_user);
        player->room_user = NULL;
    }

    if (player->messenger != NULL) {
        messenger_dispose(player->messenger);
        player->messenger = NULL;
    }

    if (player->inventory != NULL) {
        inventory_dispose(player->inventory);
        player->inventory = NULL;
    }

    if (player->player_data != NULL) {
        player_data_cleanup(player->player_data);
        player->player_data = NULL;
    }

    free(player->ip_address);
    free(player->stream);
    free(player);
}

void player_data_cleanup(player_data *player_data) {
    free(player_data->username);
    free(player_data->password);
    free(player_data->figure);
    free(player_data->pool_figure);
    free(player_data->motto);
    free(player_data->console_motto);
    free(player_data->sex);
    free(player_data);
}