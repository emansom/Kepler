#include "shared.h"
#include "hashtable.h"
#include "configuration.h"

#define CONFIGURATION_FILE "configuration.conf"

void configuration_init() {
    hashtable_new(&global.configuration.entries);

    FILE *file = fopen(CONFIGURATION_FILE, "r");

    if (!file) {
        print_info("Configuration file does not exist, creating...\n");
        print_info("\n");

        configuration_new();
        file = fopen(CONFIGURATION_FILE, "r");
    }

    configuration_read(file);

    if (file != NULL) {
        fclose(file);
    }
}

void configuration_new() {
    FILE *fp = fopen(CONFIGURATION_FILE, "wb");
    fprintf(fp, "[Database]\n");
    fprintf(fp, "database.filename=%s\n", "Kepler.db");
    fprintf(fp, "\n");
    fprintf(fp, "[Server]\n");
    fprintf(fp, "server.port=%i\n", 12321);
    fprintf(fp, "server.ip.address=%s\n", "127.0.0.1");
    fprintf(fp, "\n");
    fprintf(fp, "[Game]\n");
    fprintf(fp, "sso.tickets.enabled=%s\n", "1");
    fprintf(fp, "welcome.message.enabled=%s\n", "1");
    fprintf(fp, "welcome.message.content=%s\n", "Hello, %username%! And welcome to the Kepler server!");
    fclose(fp);
}

void configuration_read(FILE *file) {
    char *line = NULL;
    size_t len = 0;
    ssize_t read;

    while ((read = getline(&line, &len, file)) != -1) {
        if (line == NULL) {
            continue;
        }

        filter_vulnerable_characters(&line, true);
        char *found = strstr(line, "=" );

        if (found != NULL) {
            int index = (int) (found - line);

            char *key = strdup(line);
            key[index] = '\0';

            char *value = strdup(line + index + 1);
            hashtable_add(global.configuration.entries, key, value);
        }
    }

    free(line);
}

char *configuration_get(char *key) {
    if (hashtable_contains_key(global.configuration.entries, key)) {
        char *value;
        hashtable_get(global.configuration.entries, key, (void*)&value);

        if (is_numeric(value)) {
            return NULL;
        } else {
            return value;
        }
    }
}

int configuration_get_number(char *key) {
    if (hashtable_contains_key(global.configuration.entries, key)) {
        char *value;
        hashtable_get(global.configuration.entries, key, (void *) &value);

        if (is_numeric(value)) {
            return (int) strtol(value, NULL, 10);
        }
    }

    return 0;
}