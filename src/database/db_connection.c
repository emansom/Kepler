#include <stdbool.h>
#include "sqlite3.h"
#include "main.h"
#include "shared.h"

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
            fread (buffer, sizeof(char), length, f);
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
    int run_query = 0;
    char *err_msg = 0;

    if (!(file = fopen(configuration_get_string("database.filename"), "r"))) {
        print_info("Database does not exist, creating...\n");
        run_query = 1;
    }

    sqlite3 *db;

    int rc = sqlite3_open_v2(configuration_get_string("database.filename"), &db, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE | SQLITE_OPEN_FULLMUTEX, NULL);

    if (rc != SQLITE_OK) {
        fprintf(stderr, "Cannot open database: %s\n", sqlite3_errmsg(db));
        sqlite3_close(db);
        return NULL;
    } else {
        if (run_query) {
            print_info("Executing queries...\n");

            char *buffer = load_file("kepler.sql");
            rc = sqlite3_exec(db, buffer, 0, 0, &err_msg);

            if (rc != SQLITE_OK ) {
                fprintf(stderr, "SQL error: %s\n", err_msg);
                sqlite3_free(err_msg);
                sqlite3_close(db);
            }

            free(buffer);
        }
    }

    if (file != NULL) {
        fclose(file);
    }

    return db;
}

/**
 * Execute a string given to the database
 *
 * @param query the query to execute
 */
int db_execute_query(char *query) {
    char *err_msg = 0;
    int rc = sqlite3_exec(global.DB, query, 0, 0, &err_msg);

    if (rc != SQLITE_OK ) {
        fprintf(stderr, "SQL error: %s\n", err_msg);
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
        fprintf(stderr, "Failed to prepare statement: %s\n", sqlite3_errmsg(conn));

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
        fprintf(stderr, "Could not finalize (cleanup) stmt. %s\n", sqlite3_errmsg(conn));

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
        fprintf(stderr, "Could not step (execute) stmt. %s\n", sqlite3_errmsg(conn));

        // Cleanup
        sqlite3_finalize(stmt);
        sqlite3_close(conn);
        dispose_program();

        // We exit on error to keep ACID consistency
        exit(EXIT_FAILURE);
    }
    return status;
}