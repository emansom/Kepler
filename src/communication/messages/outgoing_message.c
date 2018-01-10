#include "outgoing_message.h"

#include "util/stringbuilder.h"

#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"

/**
 * Creates an outgoing message struct with the specified header
 * @param header the b64 header
 * @return the outgoing message
 */
outgoing_message *om_create(int header) {
    outgoing_message *om = malloc(sizeof(outgoing_message));
    om->header_id = header;
    om->header = base64_encode(om->header_id, 2);
    om->finalised = 0;
    om->sb = sb_create();

    sb_add_string(om->sb, om->header);
    return om;
}

/**
 * Writes a string to the outgoing message
 * @param om the outgoing message
 * @param str the string to write
 */
void om_write_str(outgoing_message *om, char *str) {
    sb_add_string(om->sb, str);
    sb_add_string(om->sb, "\2");
}

/**
 * Writes a int to the outgoing message
 * @param om the outgoing message
 * @param str the int to write
 */
void om_write_int(outgoing_message *om, int num) {
    char *encoded = vl64_encode(num);
    sb_add_string(om->sb, encoded);
    free(encoded);
}

/**
 * Finalise the packet before sending, by adding a character suffix at the end.
 * @param om the outgoing message
 */
void om_finalise(outgoing_message *om) {
    if (om->finalised) {
        return;
    }

    sb_add_string(om->sb, "\1");
    om->finalised = 1;
}

/**
 * Cleanup any variables loaded on the heap that had to do with this struct
 * @param om the outgoing message
 */
void om_cleanup(outgoing_message *om) {
    sb_cleanup(om->sb);
    free(om->header);
    free(om);
}