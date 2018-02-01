#include <stdio.h>
#include <unistd.h>

#include "wave_task.h"

#include "game/room/room_user.h"

void wave_task(room_user *room_user) {
	room_user_add_status(room_user, "wave", "");
	room_user->needs_update = 1;
	
	usleep(2000*1000);

	room_user_remove_status(room_user, "wave");
	room_user->needs_update = 1;
}
