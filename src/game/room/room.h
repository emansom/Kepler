#ifndef ROOM_H
#define ROOM_H

typedef struct room_data_s {
    char *name;
    char *description;
} room_data;

typedef struct room_s {
    int room_id;
    room_data *room_data;
} room;

room *room_create(int);

#endif