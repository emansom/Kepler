#include "shared.h"

#include "hashtable.h"

#include "external_texts_manager.h"

void texts_manager_parse(char*);

/**
 * Create a new hashtable to parsed external texts
 */
void texts_manager_init() {
    hashtable_new(&global.texts_manager.texts);
    texts_manager_parse("external_texts.txt");
}

/**
 * Parse the external texts.
 *
 * @param file_name the file name of the external texts
 */
void texts_manager_parse(char *file_name) {
    char file_path[30];
    sprintf(file_path, "%s", file_name);

    FILE *file = fopen(file_path, "r");

    if (!file) {
        return;
    }

    char *line = NULL;
    size_t len = 0;
    ssize_t read;

    while ((read = getline(&line, &len, file)) != -1) {
        char *key = get_argument(line, "=", 0);
        char *value = get_argument(line, "=", 1);

        key[strcspn(key, "\n")] = 0; // strip character
        value[strcspn(value, "\n")] = 0; // strip character

        filter_vulnerable_characters(&key, true);
        filter_vulnerable_characters(&value, true);

        hashtable_add(global.texts_manager.texts, key, value);
    }

    free(line);
}

/**
 * Get text entry by the key
 *
 * @param room_id the room id
 * @return the room
 */
char *texts_manager_get_value_by_id(char *key) {
    char *value = NULL;

    if (hashtable_contains_key(global.texts_manager.texts, key)) {
        hashtable_get(global.texts_manager.texts, key, (void *)&value);
    }

    return value;
}
