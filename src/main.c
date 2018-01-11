#include "shared.h"
#include "dyad.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"
#include "database/mysql_connection.h"

int main(void) {
    printf("Kepler Habbo server...\n");
    printf("Written by Quackster\n");

    strcpy(db_connection_settings.hostname, "localhost");
    strcpy(db_connection_settings.username, "root");
    strcpy(db_connection_settings.password, "");
    strcpy(db_connection_settings.database, "hello");

    mysql_create_connection();
    player_manager_init();
    mh_add_messages();

    dyad_Stream *dyad = create_server();
    listen_server(dyad);

    free(dyad);
    return 0;
}