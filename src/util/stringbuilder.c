#include "util/stringbuilder.h"

/**
 * Creates a stringbuilder instance
 * @return the stringbuilder
 */
stringbuilder *sb_create() {
    stringbuilder *sb = malloc(sizeof(stringbuilder));
    sb->capacity = 1024;
    sb->data = malloc(sb->capacity * sizeof(char));
    sb->index = 0;
    return sb;
}

/**
 * Checks the capacity of the buffer, if there is not a sufficient amount, the buffer will be expanded.
 * @param sb the stringbuilder
 * @param length the new length required
 */
void sb_ensure_capacity(stringbuilder *sb, int length) {
    if ((sb->capacity - sb->index) >= length) {
        // stringbuilder has capacity
        return;
    }

    sb->capacity = sb->capacity + length;
    sb->data = realloc(sb->data, sb->capacity * sizeof(char));
}

/**
 * Adds a string to the stringbuilder
 * @param sb the stringbuilder
 * @param data the string
 */
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

/**
 * Adds an integer to the stringbuilder
 * @param sb the stringbuilder
 * @param integer the int
 */
void sb_add_int(stringbuilder *sb, int integer) {
    char data[11];
    sprintf(data, "%i", integer);
    data[10] = '\0';
    sb_add_string(sb, data);
}

/**
 * Adds an integer to the stringbuilder
 * @param sb the stringbuilder
 * @param integer the int
 */
void sb_add_char(stringbuilder *sb, int num) {
    char data[2];
    data[0] = (char)num;
    data[1] = '\0';
    sb_add_string(sb, data);
}

/**
 * Cleanup any variables loaded on the heap that had to do with this struct
 * @param sb the stringbuilder
 */
void sb_cleanup(stringbuilder *sb) {
    free(sb->data);
    free(sb);
}