#ifndef PLAYER_H
#define PLAYER_H

typedef struct outgoing_message_s outgoing_message;
typedef struct room_user_s room_user;

typedef struct player_data_s {
    int id;
    char *username;
    char *figure;
    int credits;
    char *motto;
    char *sex;
    int tickets;
    int film;
} player_data;

typedef struct player_s {
    void *stream;
    player_data *player_data;
    room_user *room_user;
} player;


player *player_create(void *);
player_data *player_create_data(int, char*, char*, int, char*, char*, int, int);
void player_login(player*);

void player_cleanup(player*);
void player_send(player*, outgoing_message*);

void send_localised_error(player*, char*);
void send_alert(player*, char*);

#endif