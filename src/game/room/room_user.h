#ifndef ROOM_USER_H
#define ROOM_USER_H

typedef struct player_s player;
typedef struct room_s room;
typedef struct deque_s Deque;
typedef struct coord_s coord;
typedef struct outgoing_message_s outgoing_message;

typedef struct room_user_s {
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
} room_user;

room_user *room_user_create();
void walk_to(room_user *room_user, int, int);
void append_user_list(outgoing_message*, player*);
void append_user_status(outgoing_message *players, player *player);
void room_user_cleanup(room_user*);

#endif