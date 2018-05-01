#include "list.h"
#include "sqlite3.h"

#include "room_query.h"
#include "room_favourites_query.h"

#include "database/db_connection.h"
#include "shared.h"

/**
 * Gets the user ID for whether they have a favourite setting for that room or not.
 *
 * @param room_id the room id to check for
 * @param player_id the player id to check for
 * @return the user id, if successful, -1 if unsuccessful
 */
int room_query_check_favourite(int room_id, int player_id) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int VOTED = -1;
    int status = sqlite3_prepare_v2(conn, "SELECT user_id FROM user_room_favourites WHERE user_id = ? AND room_id = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, player_id);
        sqlite3_bind_int(stmt, 2, room_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        VOTED = sqlite3_column_int(stmt, 0);
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);

    return VOTED;
}

/**
 * Add a room as a favourite.
 *
 * @param room_id the room id to add a favourite for
 * @param player_id the user id of the player that selected the room as their favourite
 */
void room_query_favourite(int room_id, int player_id) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "INSERT INTO user_room_favourites (user_id,room_id) VALUES (?,?)", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, player_id);
        sqlite3_bind_int(stmt, 2, room_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}

/**
 * Remove a room favourite by given user id and room id.
 *
 * @param player_id the favourite to remove the room id for
 * @param room_id the room id to remove
 */
void room_query_remove_favourite(int room_id, int player_id) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "DELETE FROM user_room_favourites WHERE user_id = ? AND room_id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, player_id);
        sqlite3_bind_int(stmt, 2, room_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
}

/**
 * Get list of room favourites by player id.
 *
 * @param player_id the player id to get the favourites for
 * @return the list of rooms
 */
List *room_query_favourites(int player_id) {
    List *favourites;
    list_new(&favourites);

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT room_id FROM user_room_favourites WHERE user_id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, player_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    while (true) {
        status = sqlite3_step(stmt);

        if (status != SQLITE_ROW) {
            break;
        }

        int room_id = sqlite3_column_int(stmt, 0);
        room *room = NULL;

        if (room_manager_get_by_id(room_id) != NULL) {
            room = room_manager_get_by_id(room_id);
        } else {
            room = room_query_get_by_room_id(room_id);
        }

        list_add(favourites, room);
    }

    return favourites;
}
