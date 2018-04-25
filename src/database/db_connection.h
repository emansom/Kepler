#ifndef DB_CONNECTION_H
#define DB_CONNECTION_H

typedef struct sqlite3 sqlite3;

sqlite3 *db_create_connection();
char *copy_str(const unsigned char*);

/*void mysql_bind(MYSQL_BIND*, int, void*, enum_field_types);
void mysql_force_close(MYSQL*, MYSQL_STMT*);
void mysql_print_error(const char *, ...);*/

#endif