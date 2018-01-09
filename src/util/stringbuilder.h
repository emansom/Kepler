#ifndef STRINGBUILDER_H
#define STRINGBUILDER_H
#include <shared.h>

typedef struct {
    char *data;
    int index;
    size_t capacity;
} stringbuilder;

stringbuilder *sb_create();
void sb_ensure_capacity(stringbuilder*, int);
void sb_add_string(stringbuilder*, const char*);
void sb_add_int(stringbuilder*, int);
void sb_cleanup(stringbuilder*);

#endif