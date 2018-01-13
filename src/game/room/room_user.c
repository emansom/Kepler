#include "stdlib.h"

#include "room_user.h"

room_user *room_user_create() {
    room_user *user = malloc(sizeof(room_user));
    user->room_id = 0;
    user->room = NULL;
    return user;
}

void room_user_join(player *player, room *room) {

}

void room_user_cleanup(room_user *room_user) {
    free(room_user);
}