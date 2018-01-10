#include <sqlite3.h>
#include "shared.h"
#include "dyad.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"

int main(void) {
    printf("Kepler Habbo server...\n");
    printf("Written by Quackster\n");

    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;

    rc = sqlite3_open("test.db", &db);

    if( rc ) {
        fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
        return(0);
    } else {
        fprintf(stderr, "Opened database successfully\n");
    }
    sqlite3_close(db);

    player_manager_init();
    mh_add_messages();

    dyad_Stream *dyad = create_server();
    listen_server(dyad);

    free(dyad);
    return 0;
}