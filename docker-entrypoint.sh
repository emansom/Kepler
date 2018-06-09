#!/bin/bash
set -e

# TODO: validation for all environment variables used

# Define database url for dbmate
export DATABASE_URL="mysql://$MYSQL_USER:$MYSQL_PASSWORD@$MYSQL_HOST:3306/$MYSQL_DATABASE"

# Wait for MariaDB to come online and apply migrations
dbmate wait && dbmate up

# Configure Kepler
if [ -z "$KEPLER_PORT" ]; then
    crudini --set /usr/src/kepler/server.ini Server server.port $KEPLER_PORT
fi

if [ -z "$KEPLER_RCON_PORT" ]; then
    crudini --set /usr/src/kepler/server.ini Rcon rcon.port $KEPLER_RCON_PORT
fi

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
