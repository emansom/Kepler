#include <stdbool.h>
#include "sqlite3.h"
#include "shared.h"

#include "db_connection.h"
#include "util/configuration/configuration.h"


/**
 * Loads the .sql file from disk to create a new db, must be manually freed.
 *
 * @param path the file to load
 * @return the file contents
 */
char* load_file(char const* path) {
    char* buffer = NULL;
    size_t length;
    FILE * f = fopen (path, "rb"); //was "rb"

    if (f) {
        fseek (f, 0, SEEK_END);
        length = (size_t )ftell (f);
        fseek (f, 0, SEEK_SET);
        buffer = (char*)malloc ((length+1)*sizeof(char));

        if (buffer) {
            fread (buffer, sizeof(char), length, f);
        }

        fclose (f);

        if (buffer != NULL) {
            buffer[length] = '\0';
        }
    }

    return buffer;
}

/**
 * Create a MySQL connection instance and select the database, will print errors if the connection
 * was not successful.
 *
 * @return the MYSQL connection
 */
sqlite3 *db_create_connection() {
    FILE *file = NULL;
    int run_query = 0;
    char *err_msg = 0;

    if (!(file = fopen(configuration_get_string("database.filename"), "r"))) {
        print_info("Database does not exist, creating...\n");
        run_query = 1;
    }

    sqlite3 *db;

    int rc = sqlite3_open_v2(configuration_get_string("database.filename"), &db, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE | SQLITE_OPEN_FULLMUTEX, NULL);

    if (rc != SQLITE_OK) {
        fprintf(stderr, "Cannot open database: %s\n", sqlite3_errmsg(db));
        sqlite3_close(db);
        return NULL;
    } else {
        if (run_query) {
            print_info("Executing queries...\n");

            char *buffer = load_file("kepler.sql");
            rc = sqlite3_exec(db, buffer, 0, 0, &err_msg);

            if (rc != SQLITE_OK ) {
                fprintf(stderr, "SQL error: %s\n", err_msg);
                sqlite3_free(err_msg);
                sqlite3_close(db);
            }

            free(buffer);
        }
    }

    // Retry for 100ms long to get a mutex lock
    //sqlite3_busy_timeout(db, 100);

    if (file != NULL) {
        fclose(file);
    }

    return db;
}

/**
 * Execute a string given to the database
 *
 * @param query the query to execute
 */
int db_execute_query(char *query) {
    char *err_msg = 0;
    int rc = sqlite3_exec(global.DB, query, 0, 0, &err_msg);

    if (rc != SQLITE_OK ) {
        fprintf(stderr, "SQL error: %s\n", err_msg);
        sqlite3_free(err_msg);
        return -1;
    };

    return sqlite3_changes(global.DB);
}

int dump_db (sqlite3 *db, char *filename)
{
    FILE *fp = NULL;

    sqlite3_stmt *stmt_table = NULL;
    sqlite3_stmt *stmt_data = NULL;

    const char *table_name = NULL;
    const char *data = NULL;
    int col_cnt = 0;

    int ret = 0;
    int index = 0;
    char cmd[4096] = {0};

    fp = fopen (filename, "w");
    if (!fp)
        return -1;

    ret = sqlite3_prepare_v2 (db, "SELECT sql,tbl_name FROM sqlite_master WHERE type = 'table';",
                              -1, &stmt_table, NULL);
    if (ret != SQLITE_OK)
        goto EXIT;

    fprintf (fp, "PRAGMA foreign_keys=OFF;\nBEGIN TRANSACTION;\n");

    ret = sqlite3_step (stmt_table);
    while (ret == SQLITE_ROW)
    {
        data = sqlite3_column_text (stmt_table, 0);
        table_name = sqlite3_column_text (stmt_table, 1);
        if (!data || !table_name)
        {
            ret = -1;
            goto EXIT;
        }

        /* CREATE TABLE statements */
        fprintf (fp, "%s;\n", data);

        /* fetch table data */
        sprintf (cmd, "SELECT * from %s;",table_name);

        ret = sqlite3_prepare_v2 (db, cmd, -1, &stmt_data, NULL);
        if (ret != SQLITE_OK)
            goto EXIT;

        ret = sqlite3_step (stmt_data);
        while (ret == SQLITE_ROW)
        {
            sprintf (cmd, "INSERT INTO \"%s\" VALUES(",table_name);
            col_cnt = sqlite3_column_count(stmt_data);
            for (index = 0; index < col_cnt; index++)
            {
                if (index)
                    strcat (cmd,",");
                data = sqlite3_column_text (stmt_data, index);

                if (data)
                {
                    if (sqlite3_column_type(stmt_data, index) == SQLITE_TEXT)
                    {
                        char *temp = strdup(data);
                        char *new_data = NULL;

                        if (strstr(temp, "'") != NULL) {
                            new_data = replace(temp, "'", "''");
                        } else {
                            new_data = strdup(temp);
                        }

                        strcat (cmd, "'");
                        strcat (cmd, new_data);
                        strcat (cmd, "'");

                        free(new_data);
                        free(temp);
                    }
                    else
                    {
                        strcat (cmd, data);
                    }
                }
                else
                    strcat (cmd, "NULL");
            }
            fprintf (fp, "%s);\n", cmd);
            ret = sqlite3_step (stmt_data);
        }

        ret = sqlite3_step (stmt_table);
    }

    /* Triggers */
    if (stmt_table)
        sqlite3_finalize (stmt_table);

    ret = sqlite3_prepare_v2 (db, "SELECT sql FROM sqlite_master WHERE type = 'trigger';",
                              -1, &stmt_table, NULL);
    if (ret != SQLITE_OK)
        goto EXIT;

    ret = sqlite3_step (stmt_table);
    while (ret == SQLITE_ROW)
    {
        data = sqlite3_column_text (stmt_table, 0);
        if (!data)
        {
            ret = -1;
            goto EXIT;
        }

        /* CREATE TABLE statements */
        fprintf (fp, "%s;\n", data);

        ret = sqlite3_step (stmt_table);
    }

    fprintf (fp, "COMMIT;\n");

    EXIT:
    if (stmt_data)
        sqlite3_finalize (stmt_data);
    if (stmt_table)
        sqlite3_finalize (stmt_table);
    if (fp)
        fclose (fp);
    return ret;
}