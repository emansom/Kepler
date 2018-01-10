#include "outgoing_message.h"

#include "util/stringbuilder.h"

#include "util/encoding/base64encoding.h"
#include "util/encoding/vl64encoding.h"

outgoing_message *om_create(int header) {
    outgoing_message *om = malloc(sizeof(outgoing_message));
    om->header_id = header;
    om->finalised = 0;
    om->sb = sb_create();

    sb_add_string(om->sb, base64_encode(om->header_id, 2));
    return om;
}

void om_write_str(outgoing_message *om, char *str) {
    sb_add_string(om->sb, str);
    sb_add_string(om->sb, "\2");
}

void om_write_int(outgoing_message *om, int num) {
    sb_add_string(om->sb, vl64_encode(num));
}

void om_finalise(outgoing_message *om) {
    if (om->finalised) {
        printf("already finalised!\n");
        return;
    }

    sb_add_string(om->sb, "\1");
    om->finalised = 1;
}

void om_cleanup(outgoing_message *om) {
    sb_cleanup(om->sb);
    free(om);
}