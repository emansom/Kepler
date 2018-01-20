#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "pthread.h"

#include "deque.h"
#include "list.h"

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

			if (run->room == NULL) {
				continue;
			}

			room *room = run->room;

			if (room == NULL) {
				free(run);
				run = NULL;
				continue;
			}

			if (room->users == NULL) {
				free(run);
				run = NULL;
				continue;
			}

			if (list_size(room->users) == 0) {
				free(run);
				run = NULL;
				continue;
			}

			if (list_size(room->users) > 0) {
				run->request(run);
				deque_add_last(global.thread_manager.tasks, run);
			}

		} else {
			usleep(500 * 1000);
		}
	}
}

/*void walk_task(void *argument, runnable *self) {
	printf("hello xd\n");
	deque_push_back(global.thread_manager.tasks, self);
}*/

void create_thread_pool() {
	deque_new(&global.thread_manager.tasks);

	/*runnable *r = malloc(sizeof(runnable));
	r->request = walk_task;
	r->argument = "xd";
	deque_push_back(global.thread_manager.tasks, r);*/

	for (int i = 0; i <4; i++) {
		pthread_t thread;
		pthread_create(&thread, NULL, thr_func, &thread);
	}
}

runnable *create_runnable() {
	runnable *r = malloc(sizeof(runnable));
	r->room = NULL;
	r->request = NULL;
	r->self = r;
	return r;
}