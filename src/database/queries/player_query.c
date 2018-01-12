#include <stdio.h>
#include <string.h>

#include "sqlite3.h"

#include "game/player/player.h"

#include "database/queries/player_query.h"
#include "database/db_connection.h"

int query_player_login(char *username, char *password) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int SUCCESS = -1;
    int status = sqlite3_prepare(conn, "SELECT id FROM users WHERE username = ? AND password = ? LIMIT 1", -1, &stmt, 0);

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

    sqlite3_finalize(stmt);
    sqlite3_close(conn);
    return SUCCESS;
}

int query_player_exists_username(char *username) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int SUCCESS = -1;
    int status = sqlite3_prepare(conn, "SELECT id FROM users WHERE username = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return step == SQLITE_ROW; // row exists
}

player_data *query_player_data(int id) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    player_data *player_data = NULL;
    int status = sqlite3_prepare(conn, "SELECT id,username,figure,credits,motto,sex,tickets,film FROM users WHERE id = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        player_data = player_create_data(
            sqlite3_column_int(stmt, 0),
            (char *) sqlite3_column_text(stmt, 1),
            (char *) sqlite3_column_text(stmt, 2),
            sqlite3_column_int(stmt, 3),
            (char *) sqlite3_column_text(stmt, 4),
            (char *) sqlite3_column_text(stmt, 5),
            sqlite3_column_int(stmt, 6),
            sqlite3_column_int(stmt, 7)
        );
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);
    return player_data;
}