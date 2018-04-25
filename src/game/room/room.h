#ifndef ROOM_H
#define ROOM_H

#include <stdbool.h>

typedef struct list_s List;
typedef struct session_s session;
typedef struct room_model_s room_model;
typedef struct outgoing_message_s outgoing_message;
typedef struct runnable_s runnable;
typedef struct room_map_s room_map;

typedef struct room_data_s {
    int id;
    int owner_id;
    char *owner_name;
    int category;
    char *name;
    char *description;
    char *model;
    room_model *model_data;
    char *ccts;
    int wallpaper;
    int floor;
    int show_name;
    bool superusers;
    int accesstype;
    char *password;
    int visitors_now;
    int visitors_max;
} room_data;

typedef struct room_s {
    int room_id;
    room_data *room_data;
    room_map *room_map;
    runnable *walking_job;
    runnable *status_job;
    List *users;
    List *items;
} room;

room *room_create(int);
room_data *room_create_data(room*, int, int, int, char*, char*, char*, char*, int, int, int, bool, int, char*, int, int);
void room_enter(room*, session*);
void room_load(room*, session*);
void room_leave(room*, session*, bool hotel_view);
void room_kickall(room*);
bool room_is_owner(room *room, int user_id);
bool room_has_rights(room *room, int user_id);
void room_refresh_rights(room *room, session *player);
void room_send(room*, outgoing_message*);
void room_dispose(room*, bool override);


#endif