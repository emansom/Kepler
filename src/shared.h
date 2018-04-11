#ifndef SHARED_H
#define SHARED_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

#include "game/items/item_manager.h"
#include "game/player/player_manager.h"
#include "game/room/mapping/room_model_manager.h"
#include "game/room/room_manager.h"
#include "game/catalogue/catalogue_manager.h"
#include "game/texts/external_texts_manager.h"
#include "game/navigator/navigator_category_manager.h"

#include "util/threading.h"

#define PREFIX "Kepler"

typedef struct server_s {
    struct thread_manager thread_manager;
    struct player_manager player_manager;
    struct room_manager room_manager;
    struct room_model_manager room_model_manager;
    struct room_category_manager room_category_manager;
    struct catalogue_manager catalogue_manager;
    struct item_manager item_manager;
    struct texts_manager texts_manager;
} server;

server global;

char *get_time_formatted();
char *get_time_formatted_custom(unsigned long);
void filter_vulnerable_characters(char**, bool);
void **testing_iterate(List*);
char *get_argument(char*, char*, int);
char *replace(const char*, char, const char*);
int valid_password(const char*, const char*);
int get_name_check_code(char*);
bool is_numeric(const char*);
bool has_numbers(const char*);
bool valid_string(char*, char*);
void print_info(const char *, ...);
void print_error(const char *, ...);
char * read_line();

#endif