#include "incoming_message.h"
#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"
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
 * Read vl64 character as an integer
 *
 * @param im the incoming message
 * @return the integer value
 */
int im_read_vl64(incoming_message *im) {
    int length;

    char data[6];
    strncpy(data, im->data + im->counter, strlen(im->data));

    int val = vl64_decode(data, &length);
    im->counter += length;

    return val;
}

/**
 * Read base64 character, use free() on this returned value after using this.
 *
 * @param im the incoming message
 * @return the base64 value
 */
char *im_read_str(incoming_message *im) {
    char *recv_length = im_read_b64(im);
    int length = base64_decode(recv_length);

    char *str = malloc(length + 1 * sizeof(char));

    for (int i = 0; i < length; i++) {
        str[i] = im->data[im->counter++];
    }

    str[length] = '\0';

    free(recv_length);
    return str;
}

/**
 * Cleanup any variables loaded on the heap that had to do with this struct
 * @param im the incoming message
 */
void im_cleanup(incoming_message *im) {
    free(im->header);
    free(im);
}