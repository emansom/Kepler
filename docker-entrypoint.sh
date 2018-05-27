#!/usr/bin/env bash
set -e

if [ -z "$MYSQL_HOST" ]; then
    crudini --set /usr/src/kepler/config.ini Database mysql.hostname $MYSQL_HOST
fi

if [ -z "$MYSQL_USER" ]; then
    crudini --set /usr/src/kepler/config.ini Database mysql.username $MYSQL_USER
fi

if [ -z "$MYSQL_PASSWORD" ]; then
    crudini --set /usr/src/kepler/config.ini Database mysql.password $MYSQL_PASSWORD
fi

if [ -z "$MYSQL_DATABASE" ]; then
    crudini --set /usr/src/kepler/config.ini Database mysql.database $MYSQL_DATABASE
fi

exec "$@"
