#include <stdio.h>
#include <string.h>

#include "sqlite3.h"

#include "game/player/player.h"

#include "database/queries/player_query.h"
#include "database/db_connection.h"

/**
 *
 * @param user_id
 * @return
 */
char *query_player_username(int user_id) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    char *username;
    int status = sqlite3_prepare(conn, "SELECT username FROM users WHERE id = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, user_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        username = strdup((char*)sqlite3_column_text(stmt, 0));
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);
    return username;
}

/**
 * Retrives the user ID if there was a successful login.
 *
 * @param username the username
 * @param password the password
 * @return the user id, -1 if not successful
 */
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

/**
 * Returns true or false if the username exists, required for registration.
 *
 * @param username the username
 * @return true, if successful
 */
int query_player_exists_username(char *username) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

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

/**
 * Get player data by user id, must be freed manually.
 *
 * @param id the user id
 * @return the player data struct
 */
player_data *query_player_data(int id) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    player_data *player_data = NULL;
    int status = sqlite3_prepare(conn, "SELECT id,username,figure,credits,motto,sex,tickets,film,rank,console_motto FROM users WHERE id = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        player_data = player_create_data(
            sqlite3_column_int(stmt, 0),
            (char*)sqlite3_column_text(stmt, 1),
            (char*)sqlite3_column_text(stmt, 2),
            sqlite3_column_int(stmt, 3),
            (char*)sqlite3_column_text(stmt, 4),
            (char*)sqlite3_column_text(stmt, 5),
            sqlite3_column_int(stmt, 6),
            sqlite3_column_int(stmt, 7),
            sqlite3_column_int(stmt, 8),
             (char*)sqlite3_column_text(stmt, 9)
        );
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);
    return player_data;
}

/**
 * Creates a new player instance.
 *
 * @param username the username
 * @param figure the figure
 * @param gender the gender
 * @param password the password
 *
 * @return the inserted player id
 */
int query_player_create(char *username, char *figure, char *gender, char *password) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "INSERT INTO users (username, password, sex, figure) VALUES (?,?,?,?)", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, strlen(username), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, password, strlen(password), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, gender, strlen(gender), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 4, figure, strlen(figure), SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
        return 1;
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return (int)sqlite3_last_insert_rowid(conn);
}

void query_player_save(player *player) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "UPDATE users SET figure = ?, sex = ?, motto = ?, credits = ?, tickets = ?, film = ? WHERE id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, player->player_data->figure, strlen(player->player_data->figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, player->player_data->sex, strlen(player->player_data->sex), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, player->player_data->motto, strlen(player->player_data->motto), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 4, player->player_data->credits);
        sqlite3_bind_int(stmt, 5, player->player_data->tickets);
        sqlite3_bind_int(stmt, 6, player->player_data->film);
        sqlite3_bind_int(stmt, 7, player->player_data->id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);
}