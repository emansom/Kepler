#ifndef PLAYER_H
#define PLAYER_H

typedef struct outgoing_message_s outgoing_message;
typedef struct room_user_s room_user;
typedef struct messenger_s messenger;

typedef struct player_data_s {
    int id;
    char *username;
    char *password;
    char *figure;
    int credits;
    char *motto;
    char *sex;
    int tickets;
    int film;
    int rank;
    char *console_motto;
} player_data;

typedef struct player_s {
    void *stream;
    char *ip_address;
    player_data *player_data;
    messenger *messenger;
    room_user *room_user;
    int disconnected;
} player;


player *player_create(void*, char*);
player_data *player_create_data(int, char*, char*, char*, int, char*, char*, int, int, int, char*);
void player_login(player*);

void player_cleanup(player*);
void player_data_cleanup(player_data*);

void player_send(player*, outgoing_message*);
void player_send_raw(player*, char*);

void send_localised_error(player*, char*);
void send_alert(player*, char*);

#endif