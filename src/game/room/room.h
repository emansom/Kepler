#ifndef ROOM_H
#define ROOM_H

#include <stdbool.h>

typedef struct room_user_s room_user;
typedef struct coord_s coord;
typedef struct list_s List;
typedef struct session_s session;
typedef struct room_model_s room_model;
typedef struct outgoing_message_s outgoing_message;
typedef struct runnable_s runnable;
typedef struct room_map_s room_map;

typedef struct rights_entry_s {
    int user_id;
} rights_entry;

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
    struct room_data_s *room_data;
    room_map *room_map;
    runnable *room_schedule_job;
    List *users;
    List *items;
    List *rights;
    unsigned long tick;
} room;

room *room_create(int);
room_data *room_create_data(room*, int, int, int, char*, char*, char*, char*, int, int, int, bool, int, char*, int, int);
rights_entry *rights_entry_create(int user_id);
rights_entry *rights_entry_find(room *room, int user_id);
void room_append_data(room *instance, outgoing_message *navigator, int player_id);
void room_load_data(room *room);
void room_kickall(room*);
bool room_is_owner(room *room, int user_id);
bool room_has_rights(room *room, int user_id);
void room_refresh_rights(room *room, session *player);
void room_send(room*, outgoing_message*);
void room_dispose(room*, bool force_dispose);
List *room_nearby_players(room *room, room_user *room_user, coord *position, int distance);


#endif