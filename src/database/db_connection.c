#include <stdbool.h>

#include "sqlite3.h"
#include "main.h"
#include "shared.h"
#include "log.h"

#include "db_connection.h"
#include "util/configuration/configuration.h"


/**
 * Loads the .sql file from disk to create a new db, must be manually freed.
 *
 * @param path the file to load
 * @return the file contents
 */
char* load_file(char const* path) {
    char* buffer = NULL;
    size_t length;
    FILE * f = fopen (path, "rb"); //was "rb"

    if (f) {
        fseek (f, 0, SEEK_END);
        length = (size_t )ftell (f);
        fseek (f, 0, SEEK_SET);
        buffer = (char*)malloc ((length+1)*sizeof(char));

        if (buffer) {
            if (!fread (buffer, sizeof(char), length, f)) {
                return NULL;
            }
        }

        fclose (f);

        if (buffer != NULL) {
            buffer[length] = '\0';
        }
    }

    return buffer;
}

/**
 * Create a MySQL connection instance and select the database, will print errors if the connection
 * was not successful.
 *
 * @return the MYSQL connection
 */
sqlite3 *db_create_connection() {
    FILE *file = NULL;
    char *err_msg = NULL;

    bool run_query = false;

    if (!(file = fopen(configuration_get_string("database.filename"), "r"))) {
        log_warn("Database does not exist, creating...");
        run_query = true;
    }

    sqlite3 *db;

    // Open database in read/write mode, create if not exists and use serialized mode
    // In serialized mode (FULLMUTEX) a connection can be shared across N threads
    // without having to worry about any synchronization or locking
    int rc = sqlite3_open_v2(configuration_get_string("database.filename"), &db, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE | SQLITE_OPEN_FULLMUTEX, NULL);

    if (rc != SQLITE_OK) {
        log_fatal("Cannot open database: %s", sqlite3_errmsg(db));
        sqlite3_close(db);
        return NULL;
    } else {
        if (run_query) {
            log_info("Executing queries...");

            char *buffer = load_file("kepler.sql");
            rc = sqlite3_exec(db, buffer, 0, 0, &err_msg);

            if (rc != SQLITE_OK ) {
                log_fatal("SQL error: %s", err_msg);
                sqlite3_free(err_msg);
                sqlite3_close(db);
            }

            free(buffer);
        }
    }

    if (file != NULL) {
        fclose(file);
    }

    // The CMS might be locking the database file for a small period of legitimate
    // Therefore we define a timeout of 300ms
    // 300ms to handle slow mediums like NTFS on a spinning 5400rpm disk
    sqlite3_busy_timeout(db, 300);

    return db;
}

/**
 * Execute a string given to the database
 *
 * @param query the query to execute
 */
int db_execute_query(char *query) {
    char *err_msg;
    int rc = sqlite3_exec(global.DB, query, 0, 0, &err_msg);

    if (rc != SQLITE_OK ) {
        log_fatal("SQL error: %s", err_msg);
        sqlite3_free(err_msg);
        return -1;
    };

    return sqlite3_changes(global.DB);
}

/**
 * Check return status of prepare, log if not okay
 *
 * @param status return value of sqlite3_prepare_v2
 * @param conn SQLite3 connection
 */
int db_check_prepare(int status, sqlite3 *conn) {
    if (status != SQLITE_OK) {
        log_fatal("Failed to prepare statement: %s", sqlite3_errmsg(conn));

        // Cleanup
        sqlite3_close(conn);
        dispose_program();

        // We exit on error to keep ACID consistency
        exit(EXIT_FAILURE);
    }
    return status;
}

/**
 * Check return status of finalize, log if not okay
 *
 * @param status return value of sqlite3_finalize
 * @param conn SQLite3 connection
 */
int db_check_finalize(int status, sqlite3 *conn) {
    if (status != SQLITE_OK) {
        log_fatal("Could not finalize (cleanup) stmt. %s", sqlite3_errmsg(conn));

        // Cleanup
        sqlite3_close(conn);
        dispose_program();

        // We exit on error to keep ACID consistency
        exit(EXIT_FAILURE);
    }
    return status;
}

/**
 * Check return status of step, log if not okay
 *
 * @param status return value of sqlite3_step
 * @param conn SQLite3 connection
 * @param stmt SQLite3 statement
 */
int db_check_step(int status, sqlite3 *conn, sqlite3_stmt *stmt) {
    if (status != SQLITE_DONE && status != SQLITE_ROW) {
        log_fatal("Could not step (execute) stmt. %s", sqlite3_errmsg(conn));

        // Cleanup
        sqlite3_finalize(stmt);
        sqlite3_close(conn);
        dispose_program();

        // We exit on error to keep ACID consistency
        exit(EXIT_FAILURE);
    }
    return status;
}