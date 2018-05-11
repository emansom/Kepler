#ifndef CLUB_MANAGER_H
#define CLUB_MANAGER_H

#include "game/player/player.h"

void club_subscribe(session *player, int days);
void club_refresh(session *player);

#endif