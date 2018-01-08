#include "shared.h"

int starts_with(const char *string, const char *prefix) {
    while(*prefix) {
        if(*prefix++ != *string++)
            return 0;
    }

    return 1;
}