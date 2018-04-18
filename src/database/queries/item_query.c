#include <stdio.h>
#include <string.h>

#include "list.h"
#include "sqlite3.h"

#include "database/db_connection.h"
#include "database/queries/item_query.h"

#include "game/items/item.h"

List *item_query_get_inventory(int user_id) {
    List *items;
    list_new(&items);

    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int status = sqlite3_prepare(conn, "SELECT id,room_id,definition_id,x,y,z,rotation,custom_data FROM items WHERE user_id = ? AND room_id = 0", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, user_id);
    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    while (true) {
        status = sqlite3_step(stmt);

        if (status != SQLITE_ROW) {
            break;
        }

        //item *item_create(int id, int room_id, int definition_id, int x, int y, double z, int rotation, char *custom_data)
        item *item = item_create(
            sqlite3_column_int(stmt, 0),
            sqlite3_column_int(stmt, 1),
            sqlite3_column_int(stmt, 2),
            sqlite3_column_int(stmt, 3),
            sqlite3_column_int(stmt, 4),
            sqlite3_column_double(stmt, 5),
            sqlite3_column_int(stmt, 6),
            strdup((char *) sqlite3_column_text(stmt, 7))
        );

        list_add(items, item);
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);


    return items;
}

int item_query_create(int user_id, int room_id, int definition_id, int x, int y, double z, int rotation, char *custom_data) {
    sqlite3 *conn = db_create_connection();
    sqlite3_stmt *stmt;

    int item_id = -1;
    int status = sqlite3_prepare(conn, "INSERT INTO items (user_id, room_id, definition_id, x, y, z, rotation, custom_data) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", -1, &stmt, 0);

    if (status == SQLITE_OK) {
        sqlite3_bind_int(stmt, 1, user_id);
        sqlite3_bind_int(stmt, 2, room_id);
        sqlite3_bind_int(stmt, 3, definition_id);
        sqlite3_bind_int(stmt, 4, x);
        sqlite3_bind_int(stmt, 5, y);
        sqlite3_bind_double(stmt, 6, z);
        sqlite3_bind_int(stmt, 7, rotation);

        if (custom_data != NULL) {
            sqlite3_bind_text(stmt, 8, custom_data, (int) strlen(custom_data), SQLITE_STATIC);
        } else {
            sqlite3_bind_text(stmt, 8, "", 0, SQLITE_STATIC);
        }

    } else {
        fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(conn));
    }

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(conn));
        return 1;
    } else {
        item_id = (int)sqlite3_last_insert_rowid(conn);
    }

    sqlite3_finalize(stmt);
    sqlite3_close(conn);

    return item_id;
}