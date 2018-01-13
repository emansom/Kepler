#include "shared.h"
#include "sqlite3.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"
#include "database/db_connection.h"

#include "game/player/player.h"

int main(void) {
    print_info("Kepler Habbo server...\n");
    print_info("Written by Quackster \n");

    print_info("\n");
    print_info("Testing MySQL connection...\n");
    sqlite3 *con = db_create_connection();

    if (con == NULL) {
        print_info("The connection to the database was unsuccessful, program aborted!\n");
        return EXIT_FAILURE;
    } else {
        print_info("The connection to the database was successful!\n");
        sqlite3_close(con);
    }

    player_manager_init();
    model_manager_init();
    room_manager_init();
    mh_add_messages();

    start_server("127.0.0.1", 12321);
    return EXIT_SUCCESS;
}

