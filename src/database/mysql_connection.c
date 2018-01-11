#include "mysql.h"
#include "mysql_connection.h"
#include "shared.h"

MYSQL *mysql_create_connection() {
    MYSQL *con = mysql_init(NULL);

    if (con == NULL) {
        printf("%s\n", mysql_error(con));
    }

    if (mysql_real_connect(con, db_connection_settings.hostname, db_connection_settings.username, db_connection_settings.password, NULL, 0, NULL, 0) == NULL) {
        printf("%s\n", mysql_error(con));
        mysql_close(con);
    }

    if (mysql_select_db(con, db_connection_settings.database) != 0) {
        printf("%s\n", mysql_error(con));
        mysql_close(con);
    }

    return con;
}