#include <stdio.h>

#include "database/db_connection.h"

#include "sqlite3.h"
#include "list.h"

/** 
 * Get friends by user id
 * @param user_id the user id
 * @return
 */
List *messenger_query_get_friends(int user_id) {

    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "SELECT * FROM messenger_friends WHERE to_id = ? OR from_id = ?", -1, &stmt, 0);

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

        int to_id = sqlite3_column_int(stmt, 1);
        int from_id = sqlite3_column_int(stmt, 2);

        int friend_id = -1;

        if (to_id != user_id) {
            friend_id = to_id;
        } else {
            friend_id = from_id;
        }

        printf("user id: %i\n", friend_id);
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return NULL;
}