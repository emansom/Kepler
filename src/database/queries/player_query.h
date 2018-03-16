#ifndef PLAYER_QUERY_H
#define PLAYER_QUERY_H

typedef struct player_s player;
typedef struct player_data_s player_data;

char *query_player_username(int);
int query_player_id(char*);
int query_player_login(char*, char*);
int query_player_exists_username(char *username);
int query_player_create(char*,char*,char*,char*);
player_data *query_player_data(int);
void query_player_save_last_online(player*);
void query_player_save_looks(player*);
void query_player_save_motto(player*);
void query_player_currency(player*);
#endif