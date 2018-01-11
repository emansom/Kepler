#include <stdarg.h>
#include "shared.h"

int starts_with(const char *string, const char *prefix) {
    while(*prefix) {
        if(*prefix++ != *string++)
            return 0;
    }

    return 1;
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