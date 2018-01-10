#include "outgoing_message.h"

#include "util/stringbuilder.h"

#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"

outgoing_message *om_create(int header) {
    outgoing_message *om = malloc(sizeof(outgoing_message));
    om->sb = sb_create();

    sb_add_string(om->sb, base64_encode(header, 2));

    return om;
}

void om_write_str(outgoing_message *om, char *str) {
    sb_add_string(om->sb, str);
    sb_add_string(om->sb, "\2");
}

void om_finalise(outgoing_message *om) {
    sb_add_string(om->sb, "\1");
}

void om_cleanup(outgoing_message *om) {
    sb_cleanup(om->sb);
    free(om);
}