#ifndef DB_CONNECTION_H
#define DB_CONNECTION_H

typedef struct sqlite3 sqlite3;

sqlite3 *db_create_connection();
int db_execute_query(char *query);
int dump_db (sqlite3 *db, char *filename);

/*void mysql_bind(MYSQL_BIND*, int, void*, enum_field_types);
void mysql_force_close(MYSQL*, MYSQL_STMT*);
void mysql_print_error(const char *, ...);*/

#endif