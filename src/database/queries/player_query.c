#include <stdio.h>
#include <string.h>

#include "sqlite3.h"
#include "player_query.h"

#include "database/db_connection.h"

int query_player_login(char *username, char *password) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int SUCCESS = -1;
    int status = sqlite3_prepare(conn, "SELECT id FROM users WHERE username = ? AND password = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, password, -1, SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        SUCCESS = sqlite3_column_int(stmt, 0);
    }

    sqlite3_close(conn);
    return SUCCESS;
}