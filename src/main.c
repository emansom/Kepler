#include <stdbool.h>

#include "main.h"
#include "shared.h"

#include "sqlite3.h"
#include "list.h"
#include "thpool.h"
#include "log.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"
#include "database/db_connection.h"

#include "game/player/player.h"
#include "game/pathfinder/pathfinder.h"

#include "util/threading.h"
#include "util/configuration/configuration.h"

#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"

int main(void) {
    signal(SIGPIPE, SIG_IGN); // Stops the server crashing when the connection is closed immediately. Ignores signal 13.
    signal(SIGINT, (__sighandler_t) exit_program); // Handle cleanup on Ctrl-C

    log_info("Kepler Habbo server...");
    log_info("Written by Quackster");

    configuration_init();

    log_set_level(LOG_INFO);

    if (configuration_get_bool("debug")) {
        log_set_level(LOG_DEBUG);
    }

    if (!sqlite3_threadsafe()) {
        log_info("SQLite not threadsafe");
        return EXIT_FAILURE;
    } else {
        log_debug("Telling SQLite to use serialized mode");

        if (sqlite3_config(SQLITE_CONFIG_SERIALIZED) != SQLITE_OK) {
            log_fatal("Could not configurate SQLite to use serialized mode");
            return EXIT_FAILURE;
        }
    }

    log_info("Testing SQLite connection...");

    sqlite3 *con = db_create_connection();

    if (con == NULL) {
        log_info("The connection to the database was unsuccessful, program aborted!");
        sqlite3_close(con);
        return EXIT_FAILURE;
    }

    log_info("The connection to the database was successful!");

    log_debug("Telling SQLite to use WAL for journaling");

    sqlite3_stmt *stmt;

    int status = sqlite3_prepare_v2(con, "PRAGMA journal_mode=WAL;", -1, &stmt, 0);

    db_check_prepare(status, con);
    db_check_step(sqlite3_step(stmt), con, stmt);

    char* chosen_journal_mode = (char*)sqlite3_column_text(stmt, 0);

    if (strcmp(chosen_journal_mode, "wal") != 0) {
        log_warn("WAL not supported, now using: %s", chosen_journal_mode);
    }

    db_check_finalize(sqlite3_finalize(stmt), con);
    global.DB = con;

    log_info("Initialising various server managers...");

    texts_manager_init();
    player_manager_init();
    model_manager_init();
    category_manager_init();
    room_manager_init();
    item_manager_init();
    catalogue_manager_init();
    message_handler_init();
    create_thread_pool();

    server_settings *settings = malloc(sizeof(server_settings));
    strcpy(settings->ip, configuration_get_string("server.ip.address"));
    settings->port = configuration_get_number("server.port");

    pthread_t server_thread;
    start_server(settings, &server_thread);

    while (true) {
        char command[COMMAND_INPUT_LENGTH];
        fgets(command, COMMAND_INPUT_LENGTH, stdin);

        char *filter_command = (char*)command;
        filter_vulnerable_characters(&filter_command, true); // Strip unneeded characters

        if (handle_command(filter_command)) {
            break;
        }
    }

    return EXIT_SUCCESS;
}

bool handle_command(char *command) {
    if (starts_with(command, "query") || starts_with(command, "sql")) {
        int amount_to_strip = -1;

        if (starts_with(command, "sql")) {
            amount_to_strip = 4; // "sql " to remove
        } else {
            amount_to_strip = 6; // "query "  to remove
        }

        char *query_to_run = (command + amount_to_strip);

        if (strlen(query_to_run) <= 0) {
            log_error("The query was empty!\n");
            return false;
        }

        int modified_rows = db_execute_query(query_to_run);
        log_info("Executed query (%s) with modified rows: %i\n", query_to_run, modified_rows);

        return false;
    }

    if (strcmp(command, "q") == 0 || strcmp(command, "quit") == 0) {
        dispose_program();
        return true;
    }

    return false;
}

/**
 * Exits program, calls dispose_program
 */
void exit_program() {
    dispose_program();
    exit(EXIT_SUCCESS);
}

/**
 * Destroys program, clears all memory, except server listen instances.
 */
void dispose_program() {
    log_info("Shutting down server!");

    thpool_destroy(global.thread_manager.pool);

    player_manager_dispose();
    room_manager_dispose();
    model_manager_dispose();
    catalogue_manager_dispose();
    category_manager_dispose();

    if (sqlite3_close(global.DB) != SQLITE_OK) {
        log_fatal("Could not close SQLite database: %s", sqlite3_errmsg(global.DB));
    }

    log_info("Have a nice day!");
}
