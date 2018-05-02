#ifndef ROOM_ENTITY_MANAGER_H
#define ROOM_ENTITY_MANAGER_H

typedef struct room_s room;
typedef struct room_user_s room_user;

int create_instance_id(room_user*);
room_user *get_room_user_by_instance_id(room*, int);

void room_enter(room*, session*);
void room_leave(room*, session*, bool hotel_view);

void append_user_list(outgoing_message *players, session *player);
void append_user_status(outgoing_message *om, session *player);

#endif