#include "shared.h"

#include "mysql.h"
#include "database/mysql_connection.h"

#include "player_query.h"

player_data *query_player_login(char *username, char *password) {
    player_data *data = NULL;

    MYSQL *mysql = mysql_create_connection();
    MYSQL_STMT *statement = mysql_stmt_init(mysql);

    MYSQL_BIND input_bind[2];
    MYSQL_BIND result_bind[2];

    memset(input_bind, 0, sizeof(input_bind));
    memset(result_bind, 0, sizeof(result_bind));

    char *query = "SELECT `id`,`figure` FROM `users` WHERE `username` = ? AND `password` = ?";

    if(mysql_stmt_prepare(statement, query, strlen(query))){
        fprintf(stderr, "mysql_stmt_prepare(), INSERT failed, %s\n", mysql_error(mysql));
        return NULL;
    }

    mysql_bind(input_bind, 0, username, MYSQL_TYPE_STRING);
    mysql_bind(input_bind, 1, password, MYSQL_TYPE_STRING);

    if (mysql_stmt_bind_param(statement, input_bind) != 0) {
        fprintf(stderr, "mysql_stmt_bind_param() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return data;
    }

    if (mysql_stmt_execute(statement)) {
        fprintf(stderr, "mysql_stmt_execute() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return data;
    }

    int id;
    char figure[200];

    mysql_bind(result_bind, 0, &id, MYSQL_TYPE_LONG);
    mysql_bind(result_bind, 1, &figure, MYSQL_TYPE_STRING);

    if (mysql_stmt_bind_result(statement, result_bind) != 0) {
        fprintf(stderr, "mysql_stmt_bind_result() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return data;
    }

    while (!mysql_stmt_fetch(statement)) {
        // TODO: Handle retrieved data
        printf("User with id %i\n", id);
    }

    //printf("No user found with username %s and password %s\n", username, password);

    mysql_force_close(mysql, statement);
    return NULL;
}