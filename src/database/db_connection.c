#include "sqlite3.h"
#include "db_connection.h"
#include "shared.h"

/**
 * Get the connection details instance
 *
 * @return the connection details
 */
connection_details db_connection_settings() {
    connection_details settings;
    strcpy(settings.database_name, "Kepler.db");
    return settings;
}

/**
 * Create a MySQL connection instance and select the database, will print errors if the connection
 * was not successful.
 *
 * @return the MYSQL connection
 */
sqlite3 *db_create_connection() {
    sqlite3 *db;
    int rc = sqlite3_open(db_connection_settings().database_name, &db);

    if (rc != SQLITE_OK) {
        fprintf(stderr, "Cannot open database: %s\n", sqlite3_errmsg(db));
        sqlite3_close(db);
        return NULL;
    }

    return db;
}


void db_print_error(const char *format, ...) {
    print_info("MySQL Error: ");
    va_list args;
    va_start(args, format);
    vprintf(format, args);
    va_end(args);
}