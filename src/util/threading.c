#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

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
    r->room = NULL;
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
void do_room_task(runnable *run){
    room *room = (void*)run->room;

    if (room_manager_get_by_id(run->room_id) == NULL) {
        return;
    }

    if (list_size(room->users) == 0) {
        room->walking_job = NULL;
        room->status_job = NULL;
        free(run);
    } else {
        run->request(room);
        usleep((__useconds_t) run->millis*1000);

        if (room_manager_get_by_id(run->room_id) != NULL) {
            thpool_add_work(global.thread_manager.pool, (void*)do_room_task, run);
        } else {
            if (room_manager_get_by_id(run->room_id) != NULL) {
                room->walking_job = NULL;
                room->status_job = NULL;
            }
            free(run);
        }
    }
}

/**
 * Create a thread pool with 32 threads allocated.
 */
void create_thread_pool() {
    global.thread_manager.pool = thpool_init(32);
}