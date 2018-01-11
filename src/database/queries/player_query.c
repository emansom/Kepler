#include "shared.h"

#include "mysql.h"
#include "database/mysql_connection.h"

#include "player_query.h"
#include "game/player/player.h"

int query_player_login(char *username, char *password) {
    int SUCCESS = -1;

    MYSQL *mysql = mysql_create_connection();
    MYSQL_STMT *statement = mysql_stmt_init(mysql);

    MYSQL_BIND input_bind[2];
    MYSQL_BIND result_bind[1];

    memset(input_bind, 0, sizeof(input_bind));
    memset(result_bind, 0, sizeof(result_bind));

    char *query = "SELECT `id` FROM `users` WHERE `username` = ? AND `password` = ?";

    if(mysql_stmt_prepare(statement, query, strlen(query))){
        fprintf(stderr, "mysql_stmt_prepare(), INSERT failed, %s\n", mysql_error(mysql));
        return SUCCESS;
    }

    mysql_bind(input_bind, 0, username, MYSQL_TYPE_STRING);
    mysql_bind(input_bind, 1, password, MYSQL_TYPE_STRING);

    if (mysql_stmt_bind_param(statement, input_bind) != 0) {
        fprintf(stderr, "mysql_stmt_bind_param() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return SUCCESS;
    }

    if (mysql_stmt_execute(statement)) {
        fprintf(stderr, "mysql_stmt_execute() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return SUCCESS;
    }

    int id;
    mysql_bind(result_bind, 0, &id, MYSQL_TYPE_LONG);

    if (mysql_stmt_bind_result(statement, result_bind) != 0) {
        fprintf(stderr, "mysql_stmt_bind_result() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return SUCCESS;
    }

    while (!mysql_stmt_fetch(statement)) {
        SUCCESS = id;
        break;
    }

    mysql_force_close(mysql, statement);
    return SUCCESS;
}

player_data *query_player_data(int user_id) {
    player_data *data = NULL;

    MYSQL *mysql = mysql_create_connection();
    MYSQL_STMT *statement = mysql_stmt_init(mysql);

    MYSQL_BIND input_bind[1];
    MYSQL_BIND result_bind[6];

    memset(input_bind, 0, sizeof(input_bind));
    memset(result_bind, 0, sizeof(result_bind));

    char *query = "SELECT `figure`,`sex`,`motto`,`credits`,`tickets`,`film` FROM `users` WHERE `id` = ?";

    if(mysql_stmt_prepare(statement, query, strlen(query))){
        fprintf(stderr, "mysql_stmt_prepare(), INSERT failed, %s\n", mysql_error(mysql));
        return NULL;
    }

    mysql_bind(input_bind, 0, &user_id, MYSQL_TYPE_LONG);

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

    int credits, tickets, film;
    char username[150];
    char figure[200];
    char motto[50];
    char sex[2];

    mysql_bind(result_bind, 0, &figure, MYSQL_TYPE_STRING);
    mysql_bind(result_bind, 1, &sex, MYSQL_TYPE_STRING);
    mysql_bind(result_bind, 2, &motto, MYSQL_TYPE_STRING);
    mysql_bind(result_bind, 3, &credits, MYSQL_TYPE_LONG);
    mysql_bind(result_bind, 4, &tickets, MYSQL_TYPE_LONG);
    mysql_bind(result_bind, 5, &film, MYSQL_TYPE_LONG);

    if (mysql_stmt_bind_result(statement, result_bind) != 0) {
        fprintf(stderr, "mysql_stmt_bind_result() failed. Error: %s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return data;
    }

    if (!mysql_stmt_fetch(statement)) {
        strcpy(username, "hello");
        printf("User with id %i sex %s and figure %s and tickets %i and film %i\n", user_id, sex, figure, tickets, film);
        data = player_create_data(user_id, username, figure, credits, motto, sex, tickets, film);
    }

    mysql_force_close(mysql, statement);
    return data;
}