#ifndef ROOM_USER_H
#define ROOM_USER_H

#include <stdbool.h>

typedef struct player_s player;
typedef struct room_s room;
typedef struct item_s item;
typedef struct deque_s Deque;
typedef struct coord_s coord;
typedef struct outgoing_message_s outgoing_message;
typedef struct hashtable_s HashTable;

typedef struct room_user_s {
    player *player;
    int room_id;
    room *room;
    coord *current;
    coord *goal;
    coord *next;
    int head_rotation;
    int body_rotation;
    Deque *walk_list;
    int is_walking;
    int needs_update;
    HashTable *statuses;
    bool walking_lock;
} room_user;

typedef struct room_user_status_s {
    char *value;
    int sec_lifetime;
    char *action;
    int sec_action_switch;
    int sec_action_length;
    int action_expire;
    int lifetime_expire;
} room_user_status;

room_user *room_user_create(player*);
void walk_to(room_user*, int, int);
void stop_walking(room_user*);
void room_user_clear_walk_list(room_user*);
void append_user_list(outgoing_message*, player*);
void append_user_status(outgoing_message*, player*);
void room_user_reset(room_user*);
void room_user_cleanup(room_user*);
void room_user_add_status(room_user*,char*,char*,int,char*,int,int);
void room_user_remove_status(room_user*,char*);
int room_user_has_status(room_user*, char*);

#endif