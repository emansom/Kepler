#ifndef PLAYER_H
#define PLAYER_H

typedef struct dyad_Stream dyad_Stream;
typedef struct outgoing_message_s outgoing_message;

typedef struct player_s {
    dyad_Stream *stream;
} player;

player *player_create(dyad_Stream*);
void player_init(player*);
void player_cleanup(player*);
void player_send(player*, outgoing_message*);

#endif