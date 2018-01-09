#ifndef PLAYER_H
#define PLAYER_H

typedef struct dyad_Stream dyad_Stream;

typedef struct player_s {
    dyad_Stream *stream;
} player;

void player_init(player*);
void player_cleanup(player*);

#endif