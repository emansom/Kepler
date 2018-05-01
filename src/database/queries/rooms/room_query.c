#include <stdio.h>
#include <stdbool.h>

#include "sqlite3.h"

#include "list.h"
#include "hashtable.h"
#include "shared.h"

#include "game/room/room.h"
#include "game/room/room_manager.h"
#include "game/room/mapping/room_model.h"
#include "game/room/mapping/room_model_manager.h"

#include "game/navigator/navigator_category.h"
#include "game/navigator/navigator_category_manager.h"

#include "room_query.h"
#include "database/db_connection.h"

/**
 * Loads all room models and adds them into the room model manager.
 */
void room_query_get_models() {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT door_x, door_y, door_z, door_dir, heightmap, model_id, model_name FROM rooms_models", -1, &stmt, 0);

    if (db_check_prepare(status, conn) != SQLITE_OK) {
        fprintf(stderr, "Could not load models, invalid query.\n");
        return;
    }

    while (true) {
        status = db_check_step(sqlite3_step(stmt), conn, stmt);

        if (status == SQLITE_DONE) {
            break;
        }

        room_model *model = room_model_create(
            (char*)sqlite3_column_text(stmt, 5),
            (char*)sqlite3_column_text(stmt, 6),
            sqlite3_column_int(stmt, 0),
            sqlite3_column_int(stmt, 1),
            sqlite3_column_double(stmt, 2),
            sqlite3_column_int(stmt, 3),
            (char*)sqlite3_column_text(stmt, 4)
        );

        model_manager_add(model);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

/**
 * Load room categories into category manager
 */
void room_query_get_categories() {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT id, parent_id, name, public_spaces, allow_trading, minrole_access,minrole_setflatcat FROM rooms_categories", -1, &stmt, 0);

    if (status != SQLITE_OK) {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    while (true) {
        status = sqlite3_step(stmt);

        if (status != SQLITE_ROW) {
            break;
        }

        room_category *category = category_create(
                sqlite3_column_int(stmt, 0),
                sqlite3_column_int(stmt, 1),
                (char*)sqlite3_column_text(stmt, 2),
                sqlite3_column_int(stmt, 3),
                sqlite3_column_int(stmt, 4),
                sqlite3_column_int(stmt, 5),
                sqlite3_column_int(stmt, 6)
        );

        category_manager_add(category);
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}

/**
 * Gets a room instance by room id, will return NULL if no value exists.
 *
 * @param room_id the room id
 * @return the room instance
 */
room *room_query_get_by_room_id(int room_id) {
    room *instance = NULL;

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT * FROM rooms WHERE id = ? LIMIT 1", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, room_id);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    if (status == SQLITE_ROW) {
        instance = room_create(sqlite3_column_int(stmt, 0));
        room_data *room_data = room_create_data_sqlite(instance, stmt);
        instance->room_data = room_data;
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return instance;
}

/**
 * Gets all rooms as a list by owner id.
 *
 * @param owner_id the owner id of the rooms
 * @return the list of rooms
 */
List *room_query_get_by_owner_id(int owner_id) {
    List *rooms;
    list_new(&rooms);

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT * FROM rooms WHERE owner_id = ? ORDER BY id DESC", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, owner_id);
    }

    while (true) {
        status = db_check_step(sqlite3_step(stmt), conn, stmt);

        if (status == SQLITE_DONE) {
            break;
        }

        int room_id = sqlite3_column_int(stmt, 0);
        room *room = NULL;

        if (room_manager_get_by_id(room_id) != NULL) {
            room = room_manager_get_by_id(room_id);
        } else {
            room = room_create(sqlite3_column_int(stmt, 0));
            room->room_data = room_create_data_sqlite(room, stmt);
        }

        list_add(rooms, room);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return rooms;
}

/**
 * Gets recently created rooms within a given catgeory, and the limit of rooms to select.
 *
 * @param limit the limit of rows to select
 * @return the list of recently created rooms
 */
List *room_query_recent_rooms(int limit, int category_id) {
    List *rooms;
    list_new(&rooms);

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT * FROM rooms WHERE category = ? AND owner_id > 0 ORDER BY id DESC LIMIT ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, category_id);
        sqlite3_bind_int(stmt, 2, limit);
    }

    while (true) {
        status = db_check_step(sqlite3_step(stmt), conn, stmt);

        if (status == SQLITE_DONE) {
            break;
        }

        int room_id = sqlite3_column_int(stmt, 0);
        room *room = NULL;

        if (room_manager_get_by_id(room_id) != NULL) {
            room = room_manager_get_by_id(room_id);
        } else {
            room = room_create(sqlite3_column_int(stmt, 0));
            room->room_data = room_create_data_sqlite(room, stmt);
        }

        list_add(rooms, room);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return rooms;
}

/**
 * Get random rooms, this doesn't include public rooms.
 *
 * @param limit the limit of random rooms to select.
 * @return the list of random rooms
 */
List *room_query_random_rooms(int limit) {
    List *rooms;
    list_new(&rooms);

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT * FROM rooms WHERE owner_id > 0 ORDER BY RANDOM() LIMIT ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, limit);
    }

    while (true) {
        status = db_check_step(sqlite3_step(stmt), conn, stmt);

        if (status == SQLITE_DONE) {
            break;
        }

        int room_id = sqlite3_column_int(stmt, 0);
        room *room = NULL;

        if (room_manager_get_by_id(room_id) != NULL) {
            room = room_manager_get_by_id(room_id);
        } else {
            room = room_create(sqlite3_column_int(stmt, 0));
            room->room_data = room_create_data_sqlite(room, stmt);
        }

        list_add(rooms, room);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return rooms;
}

void query_room_save(room *room) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE rooms SET category = ?, name = ?, description = ?, wallpaper = ?, floor = ?, showname = ?, superusers = ?, accesstype = ?, password = ?, visitors_max = ? WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, room->room_data->category);
        sqlite3_bind_text(stmt, 2, room->room_data->name, strlen(room->room_data->name), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, room->room_data->description, strlen(room->room_data->description), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 4, room->room_data->wallpaper);
        sqlite3_bind_int(stmt, 5, room->room_data->floor);
        sqlite3_bind_int(stmt, 6, room->room_data->show_name);
        sqlite3_bind_int(stmt, 7, room->room_data->superusers);
        sqlite3_bind_int(stmt, 8, room->room_data->accesstype);
        sqlite3_bind_text(stmt, 9, room->room_data->password, strlen(room->room_data->password), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 10, room->room_data->visitors_max);
        sqlite3_bind_int(stmt, 11, room->room_data->id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

room_data *room_create_data_sqlite(room *room, sqlite3_stmt *stmt) {
    room_data *room_data = room_create_data(
            room,
            room->room_id,
            sqlite3_column_int(stmt, 1),
            sqlite3_column_int(stmt, 2),
            (char*)sqlite3_column_text(stmt, 3),
            (char*)sqlite3_column_text(stmt, 4),
            (char*)sqlite3_column_text(stmt, 5),
            (char*)sqlite3_column_text(stmt, 6),
            sqlite3_column_int(stmt, 7),
            sqlite3_column_int(stmt, 8),
            sqlite3_column_int(stmt, 9),
            sqlite3_column_int(stmt, 10),
            sqlite3_column_int(stmt, 11),
            (char*)sqlite3_column_text(stmt, 12),
            sqlite3_column_int(stmt, 13),
            sqlite3_column_int(stmt, 14)
    );

    return room_data;
}


void room_query_delete(int room_id) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "DELETE FROM rooms WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, room_id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}