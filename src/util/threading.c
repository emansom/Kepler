#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "pthread.h"
#include "thpool.h"
#include "hashtable.h"
#include "list.h"

#include "game/room/room_manager.h"
#include "game/room/room.h"

#include "shared.h"

void do_room_task(runnable *run){
	if (run == NULL) {
		return;
	}

	if (run->room_id == 0) {
		return;
	}

	room *room = room_manager_get_by_id(run->room_id);

	if (room == NULL) {
		return;
	}

	if (room->users == NULL) {
		return;
	}

	if (list_size(room->users) == 0) {
		if (run != NULL) {
			room->walking_job = NULL;
			free(run);
			run = NULL;
		}
	} else {
		run->request(room);
		usleep(500*1000);
		thpool_add_work(global.thread_manager.pool, (void*)do_room_task, run);
	}
}


void create_thread_pool() {
	global.thread_manager.pool = thpool_init(8);
}

runnable *create_runnable() {
	runnable *r = malloc(sizeof(runnable));
	r->room_id = 0;
	r->request = NULL;
	r->self = r;
	return r;
}