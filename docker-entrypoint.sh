#!/bin/bash
set -e

# Wait for MariaDB
until mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE -e"quit"; do
    echo "MariaDB is unavailable. Waiting.."
    sleep 1
done

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
