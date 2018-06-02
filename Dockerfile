FROM openjdk:10
LABEL maintainer="ewout@freedom.nl"

RUN apt update && apt install -y \
    libsodium-dev \
    mysql-client \
    bash \
    crudini \
    openjdk-11-jdk-headless \
    gradle \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir /usr/src/kepler

COPY Kepler-Server/ /usr/src/kepler/Kepler-Server/
COPY data/ /usr/src/kepler/data/
COPY settings.gradle /usr/src/kepler
COPY kepler.sql /usr/src/kepler

RUN cd /usr/src/kepler && \
    gradle build fatJar && \
    cd

RUN groupadd -g 1000 kepler && \
    useradd -r -u 1000 -g kepler kepler && \
    chown -R kepler:kepler /usr/src/kepler

RUN touch /usr/src/kepler/game.ini && \
    touch /usr/src/kepler/server.ini && \
    crudini --set /usr/src/kepler/server.ini Server server.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/server.ini Server server.port 12321 && \
    crudini --set /usr/src/kepler/server.ini Rcon rcon.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/server.ini Rcon rcon.port 12309 && \
    crudini --set /usr/src/kepler/server.ini Database mysql.hostname mariadb && \
    crudini --set /usr/src/kepler/server.ini Database mysql.username kepler && \
    crudini --set /usr/src/kepler/server.ini Database mysql.password verysecret && \
    crudini --set /usr/src/kepler/server.ini Database mysql.database kepler && \
    crudini --set /usr/src/kepler/server.ini Logging log.connections true && \
    crudini --set /usr/src/kepler/server.ini Logging log.sent.packets true && \
    crudini --set /usr/src/kepler/server.ini Logging log.received.packets true && \
    crudini --set /usr/src/kepler/server.ini Logging log.items.loaded true && \
    crudini --set /usr/src/kepler/game.ini Game roller.tick.default 6 && \
    crudini --set /usr/src/kepler/game.ini Game afk.timer.seconds 900 && \
    crudini --set /usr/src/kepler/game.ini Game sleep.timer.seconds 300 && \
    crudini --set /usr/src/kepler/game.ini Game fuck.aaron true && \
    crudini --set /usr/src/kepler/game.ini Game welcome.message.enabled false && \
    crudini --set /usr/src/kepler/game.ini Game welcome.message.content 'Hello, %username%! And welcome to the Kepler server!' && \
    crudini --set /usr/src/kepler/server.ini Console debug true && \
    mv /usr/src/kepler/server.ini /usr/src/kepler/tmp.ini && \
    cat /usr/src/kepler/tmp.ini | tr -d "[:blank:]" > /usr/src/kepler/server.ini && \
    rm /usr/src/kepler/tmp.ini && \
    mv /usr/src/kepler/game.ini /usr/src/kepler/gametmp.ini && \
    cat /usr/src/kepler/gametmp.ini | tr -d "[:blank:]" > /usr/src/kepler/game.ini && \
    rm /usr/src/kepler/gametmp.ini && \
    cat /usr/src/kepler/server.ini && \
    cat /usr/src/kepler/game.ini && \
    chown kepler:kepler /usr/src/kepler/server.ini && \
    chown kepler:kepler /usr/src/kepler/game.ini

COPY docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]

WORKDIR /usr/src/kepler

EXPOSE 12321
EXPOSE 12309

USER kepler

#STOPSIGNAL SIGINT

CMD ["java", "-jar", "Kepler-Server/build/libs/Kepler-Server-all.jar"]
