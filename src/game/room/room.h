#ifndef ROOM_H
#define ROOM_H

typedef struct list_s List;
typedef struct player_s player;
typedef struct room_model_s room_model;
typedef struct outgoing_message_s outgoing_message;

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
    int wall;
    int floor;
    int show_name;
    int superusers;
    int accesstype;
    char *password;
    int visitors_now;
    int visitors_max;
} room_data;

typedef struct room_s {
    int room_id;
    room_data *room_data;
    List *users;
} room;

room *room_create(int);
room_data *room_create_data(int, int, int, char*, char*, char*, char*, int, int, int, int, int, char*, int, int);
void room_enter(room*, player*);
void room_load(room*, player*);
void room_send(room*, outgoing_message*);
void room_cleanup(room*);

#endif