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
    List *rooms = room_query_get_by_owner_id(0);

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
 * Add a room by room id if the room doesn't exist, will fetch data
 * from the database.
 *
 * @param room_id the room id
 */
void room_manager_add(int room_id) {
    if (hashtable_contains_key(global.room_manager.rooms, &room_id)) {
        return;
    }

    room *room = room_query_get_by_room_id(room_id);
    hashtable_add(global.room_manager.rooms, &room->room_id, room);
}

/*
 * Add rooms by user id, will check if the room exists
 * before adding a new room.
 */
void room_manager_add_by_user_id(int user_id) {
    List *rooms = room_query_get_by_owner_id(user_id);

    for (size_t i = 0; i < list_size(rooms); i++) {
        room *room;
        list_get_at(rooms, i, (void*)&room);
        
        if (!hashtable_contains_key(global.room_manager.rooms, &room->room_id)) {
            hashtable_add(global.room_manager.rooms, &room->room_id, room);
        }
    }

    list_destroy(rooms);
}

/**
 * Get rooms by user id.
 * 
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
 * Get room manager by the room id.
 * 
 * @param room_id the room id
 * @return the room
 */
room *room_manager_get_by_id(int room_id) {
    room *room = NULL;

    if (room_id < 0) {
        return room;
    }

    if (hashtable_contains_key(global.room_manager.rooms, &room_id)) {
        hashtable_get(global.room_manager.rooms, &room_id, (void *)&room);
    }

    return room;
}

/**
 * Remove the room by room id.
 *
 * @param room_id the room id
 */
void room_manager_remove(int room_id) {
    if (hashtable_contains_key(global.room_manager.rooms, &room_id)) {
        hashtable_remove(global.room_manager.rooms, &room_id, NULL);
    }
}

/**
 * Sort list by room population. Highest populated rooms appear first.
 *
 * @param e1 the first room
 * @param e2 the second room
 * @return whether to sort
 */
int room_manager_sort(void const *e1, void const *e2) {
    room *i = (*((room **) e1));
    room *j = (*((room **) e2));

    size_t room_size_i = list_size(i->users);
    size_t room_size_j = list_size(j->users);

    if (room_size_i > room_size_j)
        return -1;

    if (room_size_i == room_size_j) {
        return 0;
    }

    return 1;
}

/**
 * Sort room ID listing so the rooms with the lowest ID appear first, that is, the most
 * recent.
 *
 * @param e1 the first room
 * @param e2 the second room
 * @return whether to sort
 */
int room_manager_sort_id(void const *e1, void const *e2) {
    room *i = (*((room **) e1));
    room *j = (*((room **) e2));

    if (i->room_id < j->room_id)
        return -1;

    if (i->room_id == j->room_id) {
        return 0;
    }

    return 1;
}

/**
 * Dispose room manager.
 */
void room_manager_dispose() {
    if (hashtable_size(global.room_manager.rooms) > 0) {
        HashTableIter iter;
        hashtable_iter_init(&iter, global.room_manager.rooms);

        TableEntry *entry;
        while (hashtable_iter_next(&iter, &entry) != CC_ITER_END) {
            room *room = entry->value;
            room_dispose(room, false);
        }
    }

    hashtable_destroy(global.room_manager.rooms);

}