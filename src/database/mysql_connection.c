#include "mysql.h"
#include "mysql_connection.h"
#include "shared.h"

/**
 * Get the connection details instance
 *
 * @return the connection details
 */
connection_details mysql_connection_settings() {
    connection_details settings;
    strcpy(settings.hostname, "localhost");
    strcpy(settings.username, "root");
    strcpy(settings.password, "");
    strcpy(settings.database, "kepler");
    return settings;
}

/**
 * Create a MySQL connection instance and select the database, will print errors if the connection
 * was not successful.
 *
 * @return the MYSQL connection
 */
MYSQL *mysql_create_connection() {
    MYSQL *con = mysql_init(NULL);
    connection_details conn_settings = mysql_connection_settings();

    if (con == NULL) {
        mysql_print_error("%s\n", mysql_error(con));
        return NULL;
    }

    if (mysql_real_connect(con, conn_settings.hostname, conn_settings.username, conn_settings.password, NULL, 0, NULL, 0) == NULL) {
        mysql_print_error("%s\n", mysql_error(con));
        mysql_close(con);
        return NULL;
    }

    if (mysql_select_db(con, conn_settings.database) != 0) {
        mysql_print_error("%s\n", mysql_error(con));
        mysql_close(con);
        return NULL;
    }

    return con;
}

void mysql_bind(MYSQL_BIND *result_bind, int position, void *buffer, enum_field_types type) {
    result_bind[position].buffer_type = type;
    result_bind[position].buffer = (char *)buffer;
    result_bind[position].buffer_length = sizeof((char *)buffer);
    result_bind[position].length = 0;
    result_bind[position].is_null = 0;
}

/**
 * Handler to close both connection and statement.
 *
 * @param mysql_conn the mysql connection
 * @param mysql_stmt the mysql statement
 */
void mysql_force_close(MYSQL *mysql_conn, MYSQL_STMT *mysql_stmt) {
    mysql_close(mysql_conn);
    mysql_stmt_close(mysql_stmt);
}

/**
 * Method handle to print errors relating to MySQL, operates the same as printf function.
 *
 * @param format the stringb format
 * @param ... the arguments afterwards
 */
void mysql_print_error(const char *format, ...) {
    print_info("MySQL Error: ");
    va_list args;
    va_start(args, format);
    vprintf(format, args);
    va_end(args);
}