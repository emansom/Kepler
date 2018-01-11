#include "shared.h"

#include "mysql.h"
#include "database/mysql_connection.h"

#include "player_query.h"

player_data *query_player_login(char *username, char *password) {
    player_data *data = NULL;

    MYSQL *mysql = mysql_create_connection();
    MYSQL_STMT *statement = mysql_stmt_init(mysql);

    MYSQL_BIND input_bind[1];
    MYSQL_BIND result_bind[2];

    memset(input_bind, 0, sizeof(input_bind));
    memset(result_bind, 0, sizeof(result_bind));

    char *query = "SELECT id,figure FROM users WHERE username = ?";
    mysql_stmt_prepare(statement, query, strlen(query));

    mysql_bind(input_bind, 0, username, MYSQL_TYPE_STRING);

    mysql_stmt_bind_param(statement, input_bind);
    mysql_stmt_execute(statement);

    if (mysql_stmt_bind_param(statement, input_bind) != 0) {
        mysql_force_close(mysql, statement);
        return data;
    }

    if (mysql_stmt_execute(statement)) {
        fprintf(stderr, "mysql_stmt_execute(), failed. Error:%s\n", mysql_stmt_error(statement));
        mysql_force_close(mysql, statement);
        return data;
    }

    int id;
    char figure[50];

    mysql_bind(result_bind, 0, &id, MYSQL_TYPE_LONG);
    mysql_bind(result_bind, 1, &figure, MYSQL_TYPE_STRING);

    if (mysql_stmt_bind_result(statement, result_bind) != 0) {
        mysql_force_close(mysql, statement);
        return data;
    }

    while (!mysql_stmt_fetch(statement)) {
        // TODO: Handle retrieved data
        printf("User with id %i and figure %s\n", id, figure);
    }

    //printf("No user found with username %s and password %s\n", username, password);

    mysql_force_close(mysql, statement);
    return NULL;
}