#include "shared.h"
#include "sqlite3.h"

#include "list.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"
#include "database/db_connection.h"

#include "game/player/player.h"
#include "game/pathfinder/pathfinder.h"

#include "util/threading.h"
#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"

int main(void) {
    print_info("Kepler Habbo server...\n");
    print_info("Written by Quackster \n");
    print_info("\n");
    print_info("Testing SQLite connection...\n");

    sqlite3 *con = db_create_connection();

    if (con == NULL) {
        print_info("The connection to the database was unsuccessful, program aborted!\n");
        return EXIT_FAILURE;
    } else {
        print_info("The connection to the database was successful!\n");
        sqlite3_close(con);
    }

    print_info("\n");
    print_info("Starting managers...\n");

    player_manager_init();
    model_manager_init();
    category_manager_init();
    room_manager_init();
    catalogue_manager_init();
    message_handler_init();
    create_thread_pool();

    /*char *str = strdup("QEQBIIH|QESAIIJ|SESAIIJ|RFSAIIH|QFSAIIJ|RESAIIJ|RFQAIIH|QEPBIIH|RFRAIIH|PFSAIIJ");
    char *s = "|";
    char *token;

    token = strtok(str, s);

    int i = 0;

    while( token != NULL ) {
        int len;

        int x = vl64_decode(token, &len);
        token += len;

        int y = vl64_decode(token, &len);
        token += len;

        int length = vl64_decode(token, &len);
        token += len;

        int width = vl64_decode(token, &len);
        token += len;

        int rotation = vl64_decode(token, &len);
        token += len;

        printf("%i %s %i %i %i %i private\n", i++, "queue_tile2", x, y, 7, rotation);
        token = strtok(NULL, s);
    }*/

    //printf("decoded: %i\n", base64_decode("@S"));

    print_info("Starting server...\n");
    start_server("0.0.0.0", 12321);
    
    return EXIT_SUCCESS;
}

