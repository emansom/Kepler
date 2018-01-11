#ifndef MYSQL_CONNECTION_H
#define MYSQL_CONNECTION_H

typedef struct st_mysql MYSQL;
typedef struct st_mysql_stmt MYSQL_STMT;
typedef struct st_mysql_bind MYSQL_BIND;
typedef enum enum_field_types enum_field_types;

typedef struct mysql_details_s {
    char hostname[50];
    char username[50];
    char password[50];
    char database[50];
} connection_details;

connection_details mysql_connection_settings();
MYSQL *mysql_create_connection();

void mysql_bind(MYSQL_BIND*, int, void*, enum_field_types);
void mysql_force_close(MYSQL*, MYSQL_STMT*);
void mysql_print_error(const char *, ...);

#endif