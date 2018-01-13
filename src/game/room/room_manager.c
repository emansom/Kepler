#include "shared.h"

#include "hashtable.h"
#include "list.h"

#include "room.h"
#include "database/queries/room_query.h"

/**
 * Create a new hashtable to store players
 */
void room_manager_init() {
    hashtable_new(&global.room_manager.rooms);
}

void room_manager_add_by_user_id(int user_id) {
    List *rooms = room_query_get_by_id(user_id);

    ListIter iter;
    list_iter_init(&iter, rooms);

    room *room;
    while (list_iter_next(&iter, (void*) &room) != CC_ITER_END) {
        if (!hashtable_contains_key(global.room_manager.rooms, &room->room_id)) {
            hashtable_add(global.room_manager.rooms, &room->room_id, room);
        }
    }

    list_destroy(rooms);
}

List *room_manager_get_by_user_id(int user_id) {
    List *rooms;
    list_new(&rooms);

    HashTableIter iter;
    hashtable_iter_init(&iter, global.room_manager.rooms);

    TableEntry *entry;
    while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
        list_add(rooms, entry->value);

    }

    return rooms;
}