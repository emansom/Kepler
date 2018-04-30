#include "room_task.h"
#include "shared.h"

#include "game/room/room.h"

#include "game/room/tasks/roller_task.h"
#include "game/room/tasks/status_task.h"
#include "game/room/tasks/walk_task.h"

/**
 * Room task called every 460ns.
 *
 * @param room the room for the task to run inside
 */
void room_task(room *room) {
    if ((room->tick % 500) == 0) {
        walk_task(room);
    }

    if ((room->tick % 1000) == 0) {
        status_task(room);
    }

    if ((room->tick % (configuration_get_number("roller.tick.default") * 500)) == 0) {
        do_roller_task(room);
    }

    room->tick += 500;
}