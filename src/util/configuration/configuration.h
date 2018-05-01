#ifndef CONFIGURATION_H
#define CONFIGURATION_H

typedef struct hashtable_s HashTable;

struct configuration {
    HashTable *entries;
};

void configuration_init();
void configuration_new();
void configuration_read(FILE *file);
char *configuration_get_string(char *key);
int configuration_get_number(char *key);

#endif