#include "incoming_message.h"
#include "util/encoding/base64encoding.h"

/**
 * Creates an incoming message give by a char array
 * @param message the char array
 * @return incoming_message struct
 */
incoming_message *im_create(char *message) {
    incoming_message *im = malloc(sizeof(incoming_message));
    im->data = message;
    im->counter = 0;
    im->header = im_read_b64(im);
    im->header_id = base64_decode(im->header);

}

/**
 * Read base64 character, use free() on this returned value after using this.
 *
 * @param im the incoming message
 * @return the base64 value
 */
char *im_read_b64(incoming_message *im) {
    char data[] = {
        im->data[im->counter++],
        im->data[im->counter++],
        '\0'
    };

    return strdup(data);
}

/**
 * Call method to cleanup the incoming message
 * @param im the incoming message
 */
void im_cleanup(incoming_message *im) {
    free(im->header);
    free(im);
}