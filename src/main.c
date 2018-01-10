#include <util/encoding/base64encoding.h>
#include "shared.h"
#include "lib/dyad/dyad.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"

#include "util/encoding/vl64encoding.h"

int main(void) {
    printf("Kepler Habbo server...\n");
    printf("Written by Quackster\n");

    printf("header: %i\n", base64_decode("BK"));

    player_manager_init();
    mh_add_messages();

    dyad_Stream *server = create_server();
    listen_server(server);

    free(server);
    return 0;
}