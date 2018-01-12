#ifndef PLAYER_QUERY_H
#define PLAYER_QUERY_H

typedef struct player_data_s player_data;

int query_player_login(char*, char*);
int query_player_exists_username(char *username);
player_data *query_player_data(int);

#endif