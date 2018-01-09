#include "util/stringbuilder.h"

stringbuilder *sb_create() {
    stringbuilder *sb = malloc(sizeof(stringbuilder));
    sb->capacity = 1024;
    sb->data = malloc(sb->capacity * sizeof(char));
    sb->index = 0;
    return sb;
}

void sb_ensure_capacity(stringbuilder *sb, int length) {
    if ((sb->capacity - sb->index) >= length) {
        // stringbuilder has capacity
        return;
    }

    sb->capacity = sb->capacity + length;
    sb->data = realloc(sb->data, sb->capacity * sizeof(char));
}

void sb_add_string(stringbuilder *sb, const char *data) {
    sb_ensure_capacity(sb, strlen(data) + 1); //+1 for terminated string

    for (int i = 0; i < strlen(data); i++) {
        if (data[i] == '\0') {
            break;
        }

        sb->data[sb->index++] = data[i];
    }

    // zero terminated string
    sb->data[sb->index] = '\0';
}

void sb_add_int(stringbuilder *sb, int integer) {
    char data[11];
    sprintf(data, "%i", integer);
    data[10] = '\0';
    sb_add_string(sb, data);
}

void sb_cleanup(stringbuilder *sb) {
    free(sb->data);
    free(sb);
}