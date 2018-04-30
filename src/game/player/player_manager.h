#ifndef PLAYER_MANAGER_H
#define PLAYER_MANAGER_H

typedef struct list_s List;
typedef struct session_s session;
typedef struct player_data_s player_data;

struct player_manager {
    List *players;
};

void player_manager_init();
session *player_manager_add(void*, char *ip);
void player_manager_remove(session*);
session *player_manager_find_by_name(char *name);
session *player_manager_find_by_id(int);
player_data *player_manager_get_data_by_id(int);
void player_manager_destroy_session_by_id(int player_id);
void player_manager_dispose();

#endif