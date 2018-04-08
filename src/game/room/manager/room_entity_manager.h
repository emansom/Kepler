#ifndef ROOM_ENTITY_MANAGER_H
#define ROOM_ENTITY_MANAGER_H

typedef struct room_s room;
typedef struct room_user_s room_user;

int create_instance_id(room_user*);
room_user *get_room_user_by_instance_id(room*, int);

#endif