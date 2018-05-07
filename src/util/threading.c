#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "log.h"
#include "thpool.h"
#include "hashtable.h"
#include "list.h"

#include "game/room/room_manager.h"
#include "game/room/room.h"

#include "shared.h"

/**
 * Create runnable instance
 *
 * @return runnable struct
 */
runnable *create_runnable() {
    runnable *r = malloc(sizeof(runnable));
    r->room_id = 0;
    r->request = NULL;
    r->self = r;
    r->millis = 500;
    return r;
}

/**
 * Schedule a task for the room at an interval.
 *
 * @param run the runnable task
 */
void do_room_task(runnable *run) {
    room *room = room_manager_get_by_id(run->room_id);

    if (room == NULL || list_size(room->users) == 0) {
        if (room != NULL) {
            room->room_schedule_job = NULL;
        }

        log_info("Room %i unloaded.", run->room_id);

        free(run);
        return;
    }

    run->request(room);
<<<<<<< HEAD
    usleep((useconds_t) run->millis * 1000);
=======
    usleep(run->millis * 1000);
>>>>>>> upstream/master
    thpool_add_work(global.thread_manager.pool, (void *) do_room_task, run);
}
/**
 * Create a thread pool with 32 threads allocated.
 */
void create_thread_pool() {
    global.thread_manager.pool = thpool_init(32);
}