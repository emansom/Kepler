#ifndef PLAYER_QUERY_H
#define PLAYER_QUERY_H

typedef struct session_s session;
typedef struct player_data_s player_data;

char *player_query_username(int user_id);
int player_query_id(char *username);
int player_query_login(char *username, char *password);
int player_query_sso(char *ticket);
int player_query_exists_username(char *username);
int player_query_create(char *username, char *password, char *figure, char *motto);
player_data *player_query_data(int id);
void player_query_save_last_online(session *);
void query_session_save_looks(session *player);
void player_query_save_motto(session *player);
void player_query_save_currency(session *player);
void player_query_save_tickets(int id, int tickets);
void player_query_save_club_informations(session *player);

#endif