#include "room_task.h"
#include "shared.h"

#include "game/room/room.h"

#include "roller_task.h"
#include "status_task.h"
#include "walk_task.h"

/**
 * Room task called every 460ns.
 *
 * @param room the room for the task to run inside
 */
void room_task(room *room) {
    if (room->walk_tick >= 460) {
        room->walk_tick = 0;
        walk_task(room);
    }

    if (room->roller_tick >= 3000) {
        room->roller_tick = 0;
        do_roller_task(room);
    }

    room->roller_tick += 460;
    room->walk_tick += 460;
}