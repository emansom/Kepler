#include "sqlite3.h"
#include "shared.h"

#include "db_connection.h"
#include "util/configuration/configuration.h"


/**
 *
 * @param path
 * @return
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

    if (!(file = fopen(configuration_get("database.filename"), "r"))) {
        print_info("Database does not exist, creating...\n");
        run_query = 1;
    }

    sqlite3 *db;
    int rc = sqlite3_open_v2(configuration_get("database.filename"), &db, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE | SQLITE_OPEN_NOMUTEX, NULL);

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

    // Retry for 100ms long to get a mutex lock
    sqlite3_busy_timeout(db, 100);

    if (file != NULL) {
        fclose(file);
    }

    return db;
}

void db_print_error(const char *format, ...) {
    print_info("SQLite Error: ");
    va_list args;
    va_start(args, format);
    vprintf(format, args);
    va_end(args);
}