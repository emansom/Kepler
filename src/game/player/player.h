#ifndef PLAYER_H
#define PLAYER_H

#include <stdbool.h>
typedef struct outgoing_message_s outgoing_message;

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
    unsigned long club_subscribed;
    unsigned long club_expiration;
    char *active_badge;
} player_data;

typedef struct session_s {
    void *stream;
    char *ip_address;
    struct player_data_s *player_data;
    struct messenger_s *messenger;
    struct inventory_s *inventory;
    struct room_user_s *room_user;
    bool logged_in;
    bool disconnected;
    bool ping_safe;
} session;

session *player_create(void*, char*);
player_data *player_create_data(int, char*, char*, char*, char*, int, char*, char*, int, int, int, char*, char*, unsigned long, unsigned long, char*);
void player_login(session*);
void player_disconnect(session *p);
void player_send(session *, outgoing_message *);
void session_send_credits(session*);
void session_send_tickets(session*);
void send_localised_error(session*, char*);
void send_alert(session*, char*);
void player_refresh_appearance(session *player);
void player_refresh_badges(session *player);
void player_cleanup(session*);
void player_data_cleanup(player_data*);

#endif