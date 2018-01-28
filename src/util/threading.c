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
		printf("CALLED 1! \n");
		return;
	}

	if (run->room == NULL) {
		printf("CALLED 2! \n");
		return;
	}

	room *room = run->room;

	if (room == NULL) {
		printf("CALLED 3! \n");
		return;
	}

	if (room->users == NULL) {
		printf("CALLED 4! \n");
		return;
	}

	if (list_size(room->users) == 0) {
		if (run != NULL) {
			printf("Room task has ended.\n");
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
	r->room = NULL;
	r->request = NULL;
	r->self = r;
	return r;
}