#ifndef PLAYER_QUERY_H
#define PLAYER_QUERY_H

typedef struct player_s player;
typedef struct player_data_s player_data;

char *player_query_username(int user_id);
int player_query_id(char *username);
int player_query_login(char *username, char *password);
int player_query_exists_username(char *username);
int player_query_create(char *username, char *password, char *figure, char *motto);
player_data *player_query_data(int id);
void player_query_save_last_online(player *);
void query_player_save_looks(player *player);
void player_query_save_motto(player *player);
void player_query_save_currency(player *player);
void player_query_save_tickets(int id, int tickets);

#endif