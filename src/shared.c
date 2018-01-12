#include "shared.h"

#include <stdarg.h>
#include <ctype.h>

int is_numeric(const char *s) {
    if (s == NULL || *s == '\0' || isspace(*s))
        return 0;
    char * p;
    strtod (s, &p);
    return *p == '\0';
}

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