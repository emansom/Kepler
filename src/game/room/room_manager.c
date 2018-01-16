#include "shared.h"

#include "hashtable.h"
#include "list.h"

#include "room.h"
#include "database/queries/room_query.h"

void room_manager_add_public_rooms();

/**
 * Create a new hashtable to store rooms
 */
void room_manager_init() {
    hashtable_new(&global.room_manager.rooms);
    room_manager_add_public_rooms();
}

/**
 * Add public rooms to the manager
 */
void room_manager_add_public_rooms() {
    List *rooms = room_query_get_by_id(0);

    ListIter iter;
    list_iter_init(&iter, rooms);

    room *room;
    while (list_iter_next(&iter, (void *)&room) != CC_ITER_END) {
        if (!hashtable_contains_key(global.room_manager.rooms, &room->room_id)) {
            hashtable_add(global.room_manager.rooms, &room->room_id, room);
        }
    }

    list_destroy(rooms);
}

/*
 * Add rooms by user id, will check if the room exists
 * before adding a new room.
 */
void room_manager_add_by_user_id(int user_id) {
    List *rooms = room_query_get_by_id(user_id);

    ListIter iter;
    list_iter_init(&iter, rooms);

    room *room;
    while (list_iter_next(&iter, (void *)&room) != CC_ITER_END) {
        if (!hashtable_contains_key(global.room_manager.rooms, &room->room_id)) {
            hashtable_add(global.room_manager.rooms, &room->room_id, room);
        }
    }

    list_destroy(rooms);
}

/**
 * Get rooms by user id
 * @param user_id the user id
 * @return the list of rooms
 */
List *room_manager_get_by_user_id(int user_id) {
    List *rooms;
    list_new(&rooms);

    if (hashtable_size(global.room_manager.rooms) > 0) {
        HashTableIter iter;
        hashtable_iter_init(&iter, global.room_manager.rooms);

        TableEntry *entry;
        while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
            room *room = entry->value;

            if (room->room_data->owner_id == user_id) {
                list_add(rooms, room);
            }
        }
    }

    return rooms;
}

/**
 * Get room manager by the room id
 * @param room_id the room id
 * @return the room
 */
room *room_manager_get_by_id(int room_id) {
    void *room = NULL;

    if (hashtable_contains_key(global.room_manager.rooms, &room_id)) {
        hashtable_get(global.room_manager.rooms, &room_id, (void *)&room);
    }

    return room;
}