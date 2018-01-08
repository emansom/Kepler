#include "shared.h"
#include "dyad/dyad.h"
#include "server/server_listener.h"

int main(void) {
    printf("Kepler Habbo server...\n");
    printf("Written by Quackster\n");

    dyad_Stream *server = create_server();
    listen_server(server);

    free(server);
    return 0;
}