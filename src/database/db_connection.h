#ifndef DB_CONNECTION_H
#define DB_CONNECTION_H

typedef struct sqlite3 sqlite3;
typedef struct sqlite3_stmt sqlite3_stmt;

sqlite3 *db_create_connection();
int db_execute_query(char *query);
int db_check_prepare(int status, sqlite3 *conn);
int db_check_finalize(int status, sqlite3 *conn);
int db_check_step(int status, sqlite3 *conn, sqlite3_stmt *stmt);

/*void mysql_bind(MYSQL_BIND*, int, void*, enum_field_types);
void mysql_force_close(MYSQL*, MYSQL_STMT*);
void mysql_print_error(const char *, ...);*/

#endif