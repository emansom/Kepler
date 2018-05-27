FROM openjdk:10
LABEL maintainer="ewout@freedom.nl"

RUN apt update && apt install -y \
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

RUN touch /usr/src/kepler/config.ini && \
    crudini --set /usr/src/kepler/config.ini Server server.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/config.ini Server server.port 12321 && \
    crudini --set /usr/src/kepler/config.ini Rcon rcon.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/config.ini Rcon rcon.port 12309 && \
    crudini --set /usr/src/kepler/config.ini Database mysql.hostname mariadb && \
    crudini --set /usr/src/kepler/config.ini Database mysql.username kepler && \
    crudini --set /usr/src/kepler/config.ini Database mysql.password verysecret && \
    crudini --set /usr/src/kepler/config.ini Database mysql.database kepler && \
    crudini --set /usr/src/kepler/config.ini Logging log.connections true && \
    crudini --set /usr/src/kepler/config.ini Logging log.sent.packets true && \
    crudini --set /usr/src/kepler/config.ini Logging log.received.packets true && \
    crudini --set /usr/src/kepler/config.ini Logging log.items.loaded true && \
    crudini --set /usr/src/kepler/config.ini Game sso.tickets.enabled true && \
    crudini --set /usr/src/kepler/config.ini Game roller.tick.default 6 && \
    crudini --set /usr/src/kepler/config.ini Game fuck.aaron true && \
    crudini --set /usr/src/kepler/config.ini Game welcome.message.enabled true && \
    crudini --set /usr/src/kepler/config.ini Game welcome.message.content 'Hello, %username%! And welcome to the Kepler server!' && \
    crudini --set /usr/src/kepler/config.ini Console debug true && \
    mv /usr/src/kepler/config.ini /usr/src/kepler/tmp.ini && \
    cat /usr/src/kepler/tmp.ini | tr -d "[:blank:]" > /usr/src/kepler/config.ini && \
    cat /usr/src/kepler/config.ini && \
    chown kepler:kepler /usr/src/kepler/config.ini

COPY docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]

EXPOSE 12321
EXPOSE 12309

USER kepler

CMD java -jar /usr/src/kepler/Kepler-Server/build/libs/Kepler-Server-all.jar
