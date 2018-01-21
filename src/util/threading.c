#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "pthread.h"

#include "thpool.h"

#include "deque.h"
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
			free(run);
			run = NULL;
		}
	} else {
		run->request(room);
		usleep(500*1000);
		thpool_add_work(global.thread_manager.pool, (void*)do_room_task, run);
	}
}


int threading_has_room(int room_id) {
	int deque_count = deque_size(global.thread_manager.tasks);

	for (int i = 0; i < deque_count; i++) {
		runnable *task;
		deque_get_at(global.thread_manager.tasks, i, (void*)&task);

		if (task != NULL) {
			if (task->room_id == room_id) {
				return 1;
			}
		}
	}

	return 0;
}

void create_thread_pool() {
	deque_new(&global.thread_manager.tasks);
	global.thread_manager.pool = thpool_init(8);

	/*for (int i = 0; i < 4; i++) {
		thpool_add_work(global.thread_manager.pool, (void*)sleep_2_secs, NULL);
	}*/
}

runnable *create_runnable() {
	runnable *r = malloc(sizeof(runnable));
	r->room_id = 0;
	r->request = NULL;
	r->self = r;
	return r;
}