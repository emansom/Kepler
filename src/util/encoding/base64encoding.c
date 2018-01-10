#include "shared.h"
#include "base64encoding.h"

int decode_base64(char *value) {
    int result = 0;

    for (int i = 0; i < strlen(value); i++) {
        result += ((value[i] - 0x40) << 6 * (strlen(value) - 1 - i));
    }

    return result;
}


char *encode_base64(int value, int length) {
    int subValue;

    char *encoded = malloc(2 *sizeof(char));
    int slot = 0;

    for (int i = 0; i < length; i++) {
        subValue = (value >> 6 * (length - 1 - i)) & 0x3f;
        encoded[slot++] = (char) (subValue + 0x40);
    }

    return encoded;
}