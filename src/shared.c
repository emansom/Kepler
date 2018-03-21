#include "shared.h"
#include "string.h"

#include "database/queries/player_query.h"

#include <stdarg.h>
#include <ctype.h>
#include <time.h>

char *get_time_formatted() {
    char buff[20];
    time_t now = time(NULL);
    strftime(buff, 20, "%Y-%m-%d %H:%M:%S", localtime(&now));
    return strdup(buff);
}

char *get_time_formatted_custom(unsigned long time_seconds) {
    char buff[20];
    time_t now = (time_t)time_seconds;
    strftime(buff, 20, "%Y-%m-%d %H:%M:%S", localtime(&now));
    return strdup(buff);
}

void filter_vulnerable_characters(char **str, int remove_newline) {
    char *body = *str;
    for (int i = 0; i < strlen(body); i++) {
        char ch = body[i];

        if (ch == 2 || ch == 9 || ch == 10 || ch == 12) {
            body[i] = (char)' ';
        }

        if (remove_newline) {
            if (ch == 13) {
                body[i] = (char)' ';
            }
        }
    }
}

char *get_argument(char *str, char *delim, int index) {
    char *copy = strdup(str);
    char *value = NULL;

    int i = 0;

    for (char *token = strtok(copy, delim); token; token = strtok(NULL, delim)) {
        if (i++ == index) {
            value = strdup(token);
            break;
        }
    }

    free(copy);
    return value;
}

char *strlwr(char *str) {
    unsigned char *p = (unsigned char *)str;

    while (*p) {
        *p = (unsigned char)tolower((unsigned char)*p);
        p++;
    }

    return str;
}

char *replace(const char *s, char ch, const char *repl) {
    int count = 0;
    const char *t;
    for(t=s; *t; t++)
        count += (*t == ch);

    size_t rlen = strlen(repl);
    char *res = malloc(strlen(s) + (rlen-1)*count + 1);
    char *ptr = res;
    for(t=s; *t; t++) {
        if(*t == ch) {
            memcpy(ptr, repl, rlen);
            ptr += rlen;
        } else {
            *ptr++ = *t;
        }
    }
    *ptr = 0;
    return res;
}

int valid_password(const char *username, const char *password) {
    char *temp_username = strdup(username);
    char *temp_password = strdup(username);

    int error_code = 0;

    if (strcmp(strlwr(temp_username), strlwr(temp_password)) > 0) {
        error_code = 5;
    } else if (strlen(password) < 6) {
        error_code = 1;
    } else if (strlen(password) > 10) {
        error_code = 2;
    } else if (!has_numbers(password)) {
        error_code = 4;
    }

    free(temp_password);
    free(temp_username);

    return error_code;
}

int get_name_check_code(char *username) {
    if (strlen(username) > 15 || !valid_string(username, "1234567890qwertyuiopasdfghjklzxcvbnm-+=?!@:.,$")) {
        return 2;
    } else {
        if (query_player_exists_username(username)) {
            return 4;
        } else {
            return 0;
        }
    }
}

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