#include "shared.h"

#include "util/stringbuilder.h"
#include "util/encryption/ciphering.h"

char *ciphering_generate_key() {
    int key_length = random_next(60, 65);

    stringbuilder *sb = sb_create();

    for (int i = 0; i < key_length; i++) {
        int j = 0;

        if (random_next(0, 2) == 1) {
            j = random_next(97, 123);
        } else {
            j = random_next(48, 58);
        }
        sb_add_char(sb, j);
    }

    char *key = strdup(sb->data);
    sb_cleanup(sb);

    return key;
}