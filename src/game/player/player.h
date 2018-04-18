#ifndef PLAYER_H
#define PLAYER_H

#include <stdbool.h>

typedef struct room_user_s room_user;
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

typedef struct player_s {
    void *stream;
    bool disconnected;
    char *ip_address;
    player_data *player_data;
    messenger *messenger;
    inventory *inventory;
    room_user *room_user;
    int logged_in;
} player;


player *player_create(void*, char*);
player_data *player_create_data(int, char*, char*, char*, char*, int, char*, char*, int, int, int, char*,char *);
void player_login(player*);
void player_send(player*, outgoing_message*);
void player_send_credits(player*);
void player_send_tickets(player*);
void send_localised_error(player*, char*);
void send_alert(player*, char*);
void player_cleanup(player*);
void player_data_cleanup(player_data*);

#endif