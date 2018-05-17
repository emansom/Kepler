#ifndef ROOM_TRADE_MANAGER_H
#define ROOM_TRADE_MANAGER_H

typedef struct room_user_s room_user;

void trade_manager_reset(room_user *room_user);
void trade_manager_refresh_boxes(room_user *room_user);

#endif