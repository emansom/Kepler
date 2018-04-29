#ifndef PLAYER_H
#define PLAYER_H

#include <stdbool.h>
#include "game/room/room_user.h"

typedef struct outgoing_message_s outgoing_message;
typedef struct messenger_s messenger;
typedef struct inventory_s inventory;

typedef struct player_data_s {
    int id;
    char *username;
    char *password;
    char *figure;
    char *pool_figure;
    int credits;
    char *motto;
    char *sex;
    int tickets;
    int film;
    int rank;
    char *console_motto;
    unsigned long last_online;
} player_data;

typedef struct session_s {
    void *stream;
    bool disconnected;
    char *ip_address;
    struct player_data_s *player_data;
    messenger *messenger;
    inventory *inventory;
    room_user *room_user;
    int logged_in;
} session;

session *player_create(void*, char*);
player_data *player_create_data(int, char*, char*, char*, char*, int, char*, char*, int, int, int, char*,char *);
void player_login(session*);
void player_send(session *, outgoing_message *);
void session_send_credits(session*);
void session_send_tickets(session*);
void send_localised_error(session*, char*);
void send_alert(session*, char*);
void player_cleanup(session*);
void player_data_cleanup(player_data*);

#endif