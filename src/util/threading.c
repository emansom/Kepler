#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "pthread.h"

#include "deque.h"
#include "list.h"

#include "game/room/room_manager.h"
#include "game/room/room.h"

#include "shared.h"

void *thr_func(void *arg) {
	while (true) {

		if (deque_size(global.thread_manager.tasks) > 0) {
			runnable *run;
			deque_remove_first(global.thread_manager.tasks, (void*) &run);

			usleep(500 * 1000);

			if (run == NULL) {
				continue;
			}

			if (run->room_id == 0) {
				continue;
			}

			room *room = room_manager_get_by_id(run->room_id);

			if (room == NULL) {
				continue;
			}

			if (room->users == NULL) {
				continue;
			}

			if (list_size(room->users) == 0) {
				free(run);
				run = NULL;
				continue;
			}

			if (list_size(room->users) > 0) {
				run->request(room);
				deque_add_last(global.thread_manager.tasks, run);
			}

		} else {
			usleep(500 * 1000);
		}
	}


void create_thread_pool() {
	deque_new(&global.thread_manager.tasks);

	for (int i = 0; i <4; i++) {
		pthread_t thread;
		pthread_create(&thread, NULL, thr_func, &thread);
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

runnable *create_runnable() {
	runnable *r = malloc(sizeof(runnable));
	r->room_id = 0;
	r->request = NULL;
	r->self = r;
	return r;
}