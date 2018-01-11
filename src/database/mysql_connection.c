#include "mysql.h"
#include "mysql_connection.h"
#include "shared.h"

/**
 * Create a MySQL connection instance and select the database, will print errors if the connection
 * was not successful.
 *
 * @return the MYSQL connection
 */
MYSQL *mysql_create_connection() {
    MYSQL *con = mysql_init(NULL);

    if (con == NULL) {
        mysql_print_error("%s\n", mysql_error(con));
        return NULL;
    }

    if (mysql_real_connect(con, db_connection_settings.hostname, db_connection_settings.username, db_connection_settings.password, NULL, 0, NULL, 0) == NULL) {
        mysql_print_error("%s\n", mysql_error(con));
        mysql_close(con);
        return NULL;
    }

    if (mysql_select_db(con, db_connection_settings.database) != 0) {
        mysql_print_error("%s\n", mysql_error(con));
        mysql_close(con);
        return NULL;
    }

    return con;
}

void mysql_print_error(const char *format, ...) {
    print_info("MySQL Error: ");
    va_list args;
    va_start(args, format);
    vprintf(format, args);
    va_end(args);
}