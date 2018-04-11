#include <stdio.h>
#include <string.h>

#include "hashtable.h"

#include "sqlite3.h"

#include "database/queries/furniture_query.h"
#include "database/db_connection.h"

/**
 * Get all the furniture definitions from database.
 *
 * @return the hashtable of furniture definitions
 */
HashTable *furniture_query_definitions() {
    HashTable *furniture;
    hashtable_new(&furniture);

    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "SELECT * FROM items_definitions", -1, &stmt, 0);

    if (status == SQLITE_OK) {
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    while (true) {
        status = sqlite3_step(stmt);

        if (status != SQLITE_ROW) {
            break;
        }


    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return furniture;
}