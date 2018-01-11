#ifndef MYSQL_CONNECTION_H
#define MYSQL_CONNECTION_H

typedef struct st_mysql MYSQL;
typedef struct mysql_details_s {
    char hostname[50];
    char username[50];
    char password[50];
    char database[50];
} connection_settings;

connection_settings db_connection_settings;

MYSQL *mysql_create_connection();

#endif