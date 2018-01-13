#ifndef ROOM_USER_H
#define ROOM_USER_H

typedef struct player_s player;
typedef struct room_s room;

typedef struct room_user_s {
    int room_id;
    room *room;
} room_user;

room_user *room_user_create();
void room_user_join(player*, room*);
void room_user_cleanup(room_user*);

#endif