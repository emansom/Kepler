#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>
#include <sodium.h>

#include "sqlite3.h"
#include "shared.h"
#include "main.h"
#include "log.h"

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

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, user_id);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    if (status == SQLITE_ROW) {
        username = strdup((char*)sqlite3_column_text(stmt, 0));
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

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

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    if (status == SQLITE_ROW) {
        USER_ID = sqlite3_column_int(stmt, 0);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

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

    int USER_ID = -1;
    int status = sqlite3_prepare_v2(conn, "SELECT id, password FROM users WHERE username = ? LIMIT 1", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    if (status == SQLITE_ROW) {
        USER_ID = sqlite3_column_int(stmt, 0);
        char *hashed_password = (char*)sqlite3_column_text(stmt, 1);

        // 0 is valid, -1 is unvalid
        int valid = crypto_pwhash_str_verify(hashed_password, password, strlen(password));

        if (valid == 0 && crypto_pwhash_str_needs_rehash(hashed_password, crypto_pwhash_OPSLIMIT_INTERACTIVE, crypto_pwhash_MEMLIMIT_INTERACTIVE)) {
            char hashed_password[crypto_pwhash_STRBYTES];

            // Hash password
            /*if (crypto_pwhash_str(hashed_password, password, strlen(password), crypto_pwhash_OPSLIMIT_INTERACTIVE, crypto_pwhash_MEMLIMIT_INTERACTIVE) != 0) {
                // Will only allocate 64MB, but just in case
                log_fatal("Not enough memory to hash passwords");
                exit_program();
                return -1;
            }*/

            // TODO: update password in database
        }

        // Clear password from memory securely
        sodium_memzero(password, strlen(password));

        // Wrong password
        if (valid == -1) {
            return -1;
        }
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return USER_ID;
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

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, ticket, -1, SQLITE_STATIC);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    if (status == SQLITE_ROW) {
        SUCCESS = sqlite3_column_int(stmt, 0);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

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

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, -1, SQLITE_STATIC);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return status == SQLITE_ROW; // row exists
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
    int status = sqlite3_prepare_v2(conn, "SELECT id,username,password,figure,pool_figure,credits,motto,sex,tickets,film,rank,console_motto,last_online,club_subscribed,club_expiration,active_badge FROM users WHERE id = ? LIMIT 1", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, id);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);

        if (status != SQLITE_ROW) {
            return NULL;
        }

        player_data = player_create_data(
            sqlite3_column_int(stmt, 0), // id
            (char*)sqlite3_column_text(stmt, 1),  // username
            (char*)sqlite3_column_text(stmt, 2),  // password
            (char*)sqlite3_column_text(stmt, 3),  // figure
            (char*)sqlite3_column_text(stmt, 4),  // pool_figure
            sqlite3_column_int(stmt, 5),          // credits
            (char*)sqlite3_column_text(stmt, 6),  // motto
            (char*)sqlite3_column_text(stmt, 7),  // sex
            sqlite3_column_int(stmt, 8),          // tickets
            sqlite3_column_int(stmt, 9),          // film
            sqlite3_column_int(stmt, 10),         // rank
            (char*)sqlite3_column_text(stmt, 11), // console_motto
            (char*)sqlite3_column_text(stmt, 12), // last_online
            (unsigned long long)sqlite3_column_int64(stmt, 13), // club_subscribed
            (unsigned long long)sqlite3_column_int64(stmt, 14), // club_expiration
            (char*)sqlite3_column_text(stmt, 15)
        );
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

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
    char hashed_password[crypto_pwhash_STRBYTES];

    // Hash password
    if (crypto_pwhash_str(hashed_password, password, strlen(password), crypto_pwhash_OPSLIMIT_INTERACTIVE, crypto_pwhash_MEMLIMIT_INTERACTIVE) != 0) {
        // Will only allocate 64MB, but just in case
        log_fatal("Not enough memory to hash passwords");
        exit_program();
        return -1;
    }

    // Clear password from memory securely
    sodium_memzero(password, strlen(password));

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "INSERT INTO users (username, password, sex, figure, pool_figure, last_online) VALUES (?,?,?,?,?,?)", -1, &stmt, 0);

    db_check_prepare(status, conn);

    char last_online[100];
    sprintf(last_online, "%lu", (unsigned long)time(NULL));

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, username, strlen(username), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, hashed_password, strlen(hashed_password), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, gender, strlen(gender), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 4, figure, strlen(figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 5, "", strlen(""), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 6, last_online, strlen(last_online), SQLITE_STATIC);

        status = db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    int user_id = -1;

    if (status == SQLITE_OK) {
        user_id = (int)sqlite3_last_insert_rowid(conn);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return user_id;
}

void query_session_save_looks(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET figure = ?, pool_figure = ?, sex = ? WHERE id = ?", -1, &stmt, 0);\

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, player->player_data->figure, strlen(player->player_data->figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, player->player_data->pool_figure, strlen(player->player_data->pool_figure), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 3, player->player_data->sex, strlen(player->player_data->sex), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 4, player->player_data->id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

void player_query_save_last_online(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET last_online = ? WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        char last_online[100];
        sprintf(last_online, "%lu", (unsigned long)time(NULL));

        sqlite3_bind_text(stmt, 1, last_online, strlen(last_online), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 2, player->player_data->id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

void player_query_save_motto(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET motto = ?, console_motto = ? WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1, player->player_data->motto, strlen(player->player_data->motto), SQLITE_STATIC);
        sqlite3_bind_text(stmt, 2, player->player_data->console_motto, strlen(player->player_data->console_motto), SQLITE_STATIC);
        sqlite3_bind_int(stmt, 3, player->player_data->id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

void player_query_save_currency(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET credits = ?, tickets = ?, film = ? WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, player->player_data->credits);
        sqlite3_bind_int(stmt, 2, player->player_data->tickets);
        sqlite3_bind_int(stmt, 3, player->player_data->film);
        sqlite3_bind_int(stmt, 4, player->player_data->id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

void player_query_save_tickets(int id, int tickets) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET tickets = ? WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, tickets);
        sqlite3_bind_int(stmt, 2, id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}

Array *player_query_badges(int id) {
    Array *badges;
    if (array_new(&badges) != CC_OK) {
        log_fatal("Couldn't create array to hold badges in player_query_badges");
        return NULL;
    }

    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "SELECT badge FROM users_badges WHERE user_id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    sqlite3_bind_int(stmt, 1, id);

    while (true) {
        status = db_check_step(sqlite3_step(stmt), conn, stmt);

        if (status != SQLITE_ROW) {
            break;
        }

        char *badge = strdup((char*)sqlite3_column_text(stmt, 0));

        if (array_add(badges, badge) != CC_OK) {
            log_fatal("Couldn't add badge to badges in player_query_badges");
            return NULL;
        }
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);

    return badges;
}

void player_query_save_club_informations(session *player) {
    sqlite3 *conn = global.DB;
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(conn, "UPDATE users SET club_subscribed = ?,club_expiration = ? WHERE id = ?", -1, &stmt, 0);

    db_check_prepare(status, conn);

    if (status == SQLITE_OK) {
        sqlite3_bind_int64(stmt, 1, (sqlite3_int64)player->player_data->club_subscribed);
        sqlite3_bind_int64(stmt, 2, (sqlite3_int64)player->player_data->club_expiration);
        sqlite3_bind_int(stmt, 3, player->player_data->id);

        db_check_step(sqlite3_step(stmt), conn, stmt);
    }

    db_check_finalize(sqlite3_finalize(stmt), conn);
}
