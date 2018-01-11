#ifndef MYSQL_CONNECTION_H
#define MYSQL_CONNECTION_H

typedef struct st_mysql MYSQL;
typedef struct mysql_details_s {
    char hostname[50];
    char username[50];
    char password[50];
    char database[50];
} connection_details;

connection_details mysql_connection_settings();
MYSQL *mysql_create_connection(connection_details conn_settings);
void mysql_print_error(const char *, ...);

#endif