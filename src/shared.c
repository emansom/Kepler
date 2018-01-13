#include "shared.h"

#include <stdarg.h>
#include <ctype.h>

bool is_numeric(const char *s) {
    if (s == NULL || *s == '\0' || isspace(*s))
        return false;

    char * p;
    strtod (s, &p);
    return *p == '\0';
}

bool has_numbers(const char *str) {
    for (int i = 0; i < strlen(str); i++) {
        if (isdigit(str[i])) {
            printf("hello!\n");
            return true;
        }
    }

    return false;
}

bool valid_string(char *str, char *allowed_chars) {
    bool valid = false;

    for (int j = 0; j < strlen(str); j++) {
        valid = false;

        for (int i = 0; i < strlen(allowed_chars); i++) {
            if (str[j] == allowed_chars[i]) {
                valid = true;
                continue;
            }
        }
    }

    return valid;
}

void print_info(const char *format, ...) {
    printf("[%s] ", PREFIX);
    va_list args;
    va_start(args, format);
    vprintf(format, args);
    va_end(args);
}

void print_error(const char *format, ...) {
    printf("[%s] ERROR: ", PREFIX);
    va_list args;
    va_start(args, format);
    vprintf(format, args);
    va_end(args);
}