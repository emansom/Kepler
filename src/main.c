#include "shared.h"

#include "lib/sqlite3/sqlite3.h"
#include "list.h"
#include "lib/cthreadpool/thpool.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"
#include "database/db_connection.h"

#include "game/player/player.h"
#include "game/pathfinder/pathfinder.h"

#include "util/threading.h"
#include "util/configuration/configuration.h"

#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"

void dispose_program();

int main(void) {
    signal(SIGPIPE, SIG_IGN); // Stops the server crashing when the connection is closed immediately. Ignores signal 13.

    print_info("Kepler Habbo server...\n");
    print_info("Written by Quackster \n");
    print_info("\n");

    configuration_init();
    print_info("Testing SQLite connection...\n");

    if (!sqlite3_threadsafe()) {
        print_info("SQLite not threadsafe");
        return EXIT_FAILURE;
    }

    sqlite3 *con = db_create_connection();

    if (con == NULL) {
        print_info("The connection to the database was unsuccessful, program aborted!\n");
        return EXIT_FAILURE;
    } else {
        print_info("The connection to the database was successful!\n");

        print_info("Telling SQLite to use WAL mode\n");

        sqlite3_stmt *stmt;

        int status = sqlite3_prepare(con, "PRAGMA journal_mode=WAL;", -1, &stmt, 0);

        if (status != SQLITE_OK) {
            fprintf(stderr, "Failed to execute statement: %s\n", sqlite3_errmsg(con));
        }

        if (sqlite3_step(stmt) != SQLITE_DONE) {
            printf("\nCould not step (execute) stmt. %s\n", sqlite3_errmsg(con));
        }

        sqlite3_finalize(stmt);
        sqlite3_close(con);
    }

    print_info("\n");
    print_info("Initialising various server managers...\n");

    texts_manager_init();
    player_manager_init();
    model_manager_init();
    category_manager_init();
    room_manager_init();
    item_manager_init();
    catalogue_manager_init();
    message_handler_init();
    create_thread_pool();
    print_info("\n");

    server_settings *settings = malloc(sizeof(server_settings));
    strcpy(settings->ip, configuration_get("server.ip.address"));
    settings->port = configuration_get_number("server.port");

    pthread_t server_thread;
    start_server(settings, &server_thread);

    while (true) {
        char command[50];
        fgets(command, 10, stdin);

        char *filter_command = (char*)command;
        filter_vulnerable_characters(&filter_command, true); // Strip unneeded characters

        if (strcmp(filter_command, "q") == 0 || strcmp(filter_command, "quit") == 0) {
            dispose_program();
            break;
        }
    }

    return EXIT_SUCCESS;
}

/**
 * Destroys program, clears all memory, except server listen instances.
 */
void dispose_program() {
    printf("Shutting down server!\n");
    thpool_destroy(global.thread_manager.pool);
    player_manager_dispose();
    model_manager_dispose();
    room_manager_dispose();
    catalogue_manager_dispose();
    category_manager_dispose();
    printf("Done!\n");
}
