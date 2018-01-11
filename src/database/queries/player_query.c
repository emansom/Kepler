#include "shared.h"

#include "mysql.h"
#include "database/mysql_connection.h"

#include "player_query.h"

/*
 *  MYSQL *mysql = mysql_create_connection(mysql_connection_settings());
 * char *query = "SELECT * FROM users WHERE username = ? AND password = ?";
    mysql_stmt_prepare(stmt, query, strlen(query));

    memset(ps_params, 0, sizeof (ps_params));

    unsigned long user_length = strlen(username);
    ps_params[0].buffer_type = MYSQL_TYPE_VARCHAR;
    ps_params[0].buffer = username;
    ps_params[0].length = &user_length;
    ps_params[0].is_null = 0;

    unsigned long password_length = strlen(password);
    ps_params[1].buffer_type = MYSQL_TYPE_VARCHAR;
    ps_params[1].buffer = password;
    ps_params[1].length = &password_length;
    ps_params[1].is_null = 0;*/


player_data *query_player_login(char *username, char *password) {
    player_data *data = NULL;

    MYSQL *mysql = mysql_create_connection(mysql_connection_settings());
    MYSQL_STMT *statement = mysql_stmt_init(mysql);

    char *query = "SELECT id,figure FROM users WHERE username = ?";
    mysql_stmt_prepare(statement, query, strlen(query));

    MYSQL_BIND input_bind[1];
    memset(input_bind, 0, sizeof(input_bind));

    unsigned long username_length = sizeof(username);
    input_bind[0].buffer_type = MYSQL_TYPE_VARCHAR;
    input_bind[0].buffer = username;
    input_bind[0].buffer_length = sizeof(username);
    input_bind[0].length = &username_length;
    input_bind[0].is_null = (my_bool*)0;

    mysql_stmt_bind_param(statement, input_bind);
    mysql_stmt_execute(statement);

    if (mysql_stmt_bind_param(statement, input_bind)) {
        fprintf(stderr, "ERROR:mysql_stmt_bind_param failed\n");
        exit(1);
    }

    if (mysql_stmt_execute(statement)) {
        fprintf(stderr, "mysql_stmt_execute(), failed. Error:%s\n", mysql_stmt_error(statement));
        exit(1);
    }

    /* Fetch result set meta information */
    MYSQL_RES* prepare_meta_result = mysql_stmt_result_metadata(statement);
    if (!prepare_meta_result)
    {
        fprintf(stderr,
                " mysql_stmt_result_metadata(), \
                returned no meta information\n");
        fprintf(stderr, " %s\n", mysql_stmt_error(statement));
        exit(1);
    }

    /* Bind single result column, expected to be a double. */
    MYSQL_BIND result_bind[2];
    memset(result_bind, 0, sizeof(result_bind));


    int result_id;
    char figure[50];
    unsigned long result_len = 0;

    result_bind[0].buffer_type = MYSQL_TYPE_INT24;
    result_bind[0].buffer = &result_id;
    result_bind[0].buffer_length = sizeof(result_id);
    result_bind[0].length = &result_bind[0].buffer_length;
    result_bind[0].is_null = (my_bool*)0;

    result_bind[1].buffer_type = MYSQL_TYPE_STRING;
    result_bind[1].buffer = &figure;
    result_bind[1].buffer_length = sizeof(figure);
    result_bind[1].length = &result_bind[1].buffer_length;
    result_bind[1].is_null = (my_bool*)0;

    if (mysql_stmt_bind_result(statement, result_bind)) {

    }

    while (!mysql_stmt_fetch(statement)) {
        printf("%s\n", figure);
    }


    mysql_close(mysql);
    return NULL;
}