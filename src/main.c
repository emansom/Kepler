#include "shared.h"
#include "lib/dyad/dyad.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"

#include "util/encoding/base64encoding.h"

int main(void) {
    printf("Kepler Habbo server...\n");
    printf("Written by Quackster\n");

    player_manager_init();
    mh_add_messages();

    printf("decoded: %i\n", decode_base64("CN"));

    dyad_Stream *server = create_server();
    listen_server(server);

    free(server);
    return 0;
}