#include <stdbool.h>
#include "sqlite3.h"
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