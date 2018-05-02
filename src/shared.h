#ifndef SHARED_H
#define SHARED_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include "sqlite3.h"

#include "game/items/item_manager.h"
#include "game/player/player_manager.h"
#include "game/room/mapping/room_model_manager.h"
#include "game/room/room_manager.h"
#include "game/catalogue/catalogue_manager.h"
#include "game/texts/external_texts_manager.h"
#include "game/navigator/navigator_category_manager.h"

#include "util/threading.h"
#include "util/configuration/configuration.h"

#define PREFIX "Kepler"

typedef struct sqlite3 sqlite3;

typedef struct server_s {
    struct thread_manager thread_manager;
    struct player_manager player_manager;
    struct room_manager room_manager;
    struct room_model_manager room_model_manager;
    struct room_category_manager room_category_manager;
    struct catalogue_manager catalogue_manager;
    struct item_manager item_manager;
    struct texts_manager texts_manager;
    struct configuration configuration;
    struct sqlite3 *DB;
    bool is_shutdown;
} server;

server global;

char *get_time_formatted();
char *get_short_time_formatted();
char *get_time_formatted_custom(unsigned long);
void filter_vulnerable_characters(char**, bool);
void replace_vulnerable_characters(char**, bool, char);
char *get_argument(char*, char*, int);
char* replace(char* str, char* a, char* b);
char *replace_char(const char *, char, char *);
int valid_password(const char*, const char*);
int get_name_check_code(char*);
bool is_numeric(const char*);
bool has_numbers(const char*);
bool has_allowed_characters(char *, char *);
bool starts_with(const char *pre, const char *str);

#endif