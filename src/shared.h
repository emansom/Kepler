#ifndef SHARED_H
#define SHARED_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "game/player/player_manager.h"

#define PREFIX "Kepler"

struct server {
    struct player_manager player_manager;
};

static struct server global;



void print_info(const char *, ...);
void print_error(const char *, ...);
int starts_with(const char*, const char*);

#endif