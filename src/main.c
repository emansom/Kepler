#include <util/encoding/base64encoding.h>
#include "shared.h"
#include "lib/dyad/dyad.h"

#include "server/server_listener.h"
#include "communication/message_handler.h"

#include "util/encoding/vl64encoding.h"

int main(void) {
    printf("Kepler Habbo server...\n");
    printf("Written by Quackster\n");

    /*printf("x: %s\n", vl64_encode(1337));

    int len;
    char *h = "YNE";
    printf("header: %i\n", vl64_decode(h, &len));
    printf("len: %i\n", len);*/

    //printf("header: %i\n", base64_decode("DU"));

    player_manager_init();
    mh_add_messages();

    dyad_Stream *server = create_server();
    listen_server(server);

    free(server);
    return 0;
}