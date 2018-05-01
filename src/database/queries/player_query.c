#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>

#include "sqlite3.h"
#include "shared.h"

#include "game/player/player.h"

#include "database/queries/player_query.h"
#include "database/db_connection.h"

/**
 *
 * @param user_id
 * @return
 */
char *player_query_username(int user_id) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    char *username = NULL;
    int status = sqlite3_prepare_v2(conn, "SELECT username FROM users WHERE id = ? LIMIT 1", -1, &stmt, 0);

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
    //sqlite3_close(conn);

    return username;
}

/**
 *
 * @param user_id
 * @return
 */
int player_query_id(char *username) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int USER_ID = -1;
    int status = sqlite3_prepare_v2(conn, "SELECT id FROM users WHERE username = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        USER_ID = sqlite3_column_int(stmt, 0);
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);

    return USER_ID;
}

/**
 * Retrieves the user ID if there was a successful login.
 *
 * @param username the username
 * @param password the password
 * @return the user id, -1 if not successful
 */
int player_query_login(char *username, char *password) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int SUCCESS = -1;
    int status = sqlite3_prepare_v2(conn, "SELECT id FROM users WHERE username = ? AND password = ? LIMIT 1", -1, &stmt, 0);

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
    //sqlite3_close(conn);
    return SUCCESS;
}

/**
 * Retrieves the user ID if a SSO ticket matches
 *
 * @param ticket the SSO (Single Sign On) ticket
 * @return the user id, -1 if not successful
 */
int player_query_sso(char *ticket) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int SUCCESS = -1;
    int status = sqlite3_prepare_v2(conn, "SELECT id FROM users WHERE sso_ticket = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, ticket, -1, SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    if (step == SQLITE_ROW) {
        SUCCESS = sqlite3_column_int(stmt, 0);
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
    return SUCCESS;
}

/**
 * Returns true or false if the username exists, required for registration.
 *
 * @param username the username
 * @return true, if successful
 */
int player_query_exists_username(char *username) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT id FROM users WHERE username = ? LIMIT 1", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    int step = sqlite3_step(stmt);

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);

    return step == SQLITE_ROW; // row exists
}

/**
 * Get player data by user id, must be freed manually.
 *
 * @param id the user id
 * @return the player data struct
 */
player_data *player_query_data(int id) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    player_data *player_data = NULL;
    int status = sqlite3_prepare_v2(conn, "SELECT id,username,password,figure,pool_figure,credits,motto,sex,tickets,film,rank,console_motto,last_online FROM users WHERE id = ? LIMIT 1", -1, &stmt, 0);

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
            (char*)sqlite3_column_text(stmt, 3),
            (char*)sqlite3_column_text(stmt, 4),
            sqlite3_column_int(stmt, 5),
            (char*)sqlite3_column_text(stmt, 6),
            (char*)sqlite3_column_text(stmt, 7),
            sqlite3_column_int(stmt, 8),
            sqlite3_column_int(stmt, 9),
            sqlite3_column_int(stmt, 10),
            (char*)sqlite3_column_text(stmt, 11),
            (char*)sqlite3_column_text(stmt, 12)
        );
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);

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
int player_query_create(char *username, char *figure, char *gender, char *password) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "INSERT INTO users (username, password, sex, figure, pool_figure, last_online) VALUES (?,?,?,?,?,?)", -1, &stmt, 0);

    char last_online[100];
    sprintf(last_online, "%lu", (unsigned long)time(NULL));

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, strlen(username), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, password, strlen(password), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, gender, strlen(gender), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 4, figure, strlen(figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 5, "", strlen(""), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 6, last_online, strlen(last_online), SQLITE_STATIC);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    int user_id = (int)sqlite3_last_insert_rowid(conn);

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);

    return user_id;
}

void query_session_save_looks(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET figure = ?, pool_figure = ?, sex = ? WHERE id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, player->player_data->figure, strlen(player->player_data->figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, player->player_data->pool_figure, strlen(player->player_data->pool_figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, player->player_data->sex, strlen(player->player_data->sex), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 4, player->player_data->id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}

void player_query_save_last_online(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET last_online = ? WHERE id = ?", -1, &stmt, 0);

    char last_online[100];
    sprintf(last_online, "%lu", (unsigned long)time(NULL));

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, last_online, strlen(last_online), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 2, player->player_data->id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        fprintf(stderr, "Could not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}

void player_query_save_motto(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET motto = ?, console_motto = ? WHERE id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, player->player_data->motto, strlen(player->player_data->motto), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, player->player_data->console_motto, strlen(player->player_data->console_motto), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 3, player->player_data->id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}

void player_query_save_currency(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET credits = ?, tickets = ?, film = ? WHERE id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, player->player_data->credits);
        sqlite3_bind_int(stmt, 2, player->player_data->tickets);
        sqlite3_bind_int(stmt, 3, player->player_data->film);
        sqlite3_bind_int(stmt, 4, player->player_data->id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}

void player_query_save_tickets(int id, int tickets) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET tickets = ? WHERE id = ?", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, tickets);
        sqlite3_bind_int(stmt, 2, id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
    }

    sqlite3_finalize(stmt);
    //sqlite3_close(conn);
}
