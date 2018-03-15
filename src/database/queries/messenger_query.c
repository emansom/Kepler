#include <stdio.h>

#include "database/db_connection.h"
#include "database/queries/messenger_query.h"

#include "game/messenger/messenger_friend.h"

#include "sqlite3.h"
#include "list.h"

/** 
 * Get friends by user id
 * @param user_id the user id
 * @return
 */
List *messenger_query_get_friends(int user_id) {
    List *friends;
    list_new(&friends);

    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "SELECT to_id, from_id FROM messenger_friends WHERE to_id = ? OR from_id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, user_id);
        sqlite3_bind_int(stmt, 2, user_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    while (true) {
        status = sqlite3_step(stmt);

        if (status != SQLITE_ROW) {
            break;
        }

        int to_id = sqlite3_column_int(stmt, 0);
        int from_id = sqlite3_column_int(stmt, 1);

        int friend_id = -1;

        if (to_id != user_id) {
            friend_id = to_id;
        } else {
            friend_id = from_id;
        }
        
        messenger_friend *friend = messenger_friend_create(friend_id);
        list_add(friends, (void*)friend);
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return friends;
}

/**
 * Insert a new request into the database
 * 
 * @param to_id the id that the request is sent from
 * @param from_id the id that the request is sent to
 */
int messenger_query_new_request(int from_id, int to_id) {
    if (messenger_query_request_exists(from_id, to_id)) {
        return 0;
    }

    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "INSERT INTO messenger_requests (from_id, to_id) VALUES (?, ?)", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, from_id);
        sqlite3_bind_int(stmt, 2, to_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return 1;
}

/**
 * Insert a new request into the database
 * 
 * @param to_id the id that the request is sent from
 * @param from_id the id that the request is sent to
 */
int messenger_query_request_exists(int from_id, int to_id) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int result = 0;
    int status = sqlite3_prepare(conn, "SELECT * FROM messenger_requests WHERE to_id = ? AND from_id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, to_id);
        sqlite3_bind_int(stmt, 2, from_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        result = 1;
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return result;
}