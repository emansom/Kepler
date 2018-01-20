#ifndef ROOM_USER_H
#define ROOM_USER_H

typedef struct player_s player;
typedef struct room_s room;
typedef struct deque_s Deque;

typedef struct room_user_s {
    int room_id;
    room *room;
    int x;
    int y;
    double z;
    int head_rotation;
    int body_rotation;
    Deque *walk_list;
    int is_walking;
} room_user;

room_user *room_user_create();
void append_user_list(outgoing_message*, player*);
void append_user_status(outgoing_message *players, player *player);
void room_user_cleanup(room_user*);

#endif