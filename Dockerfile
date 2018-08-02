FROM openjdk:10
LABEL maintainer="ewout@freedom.nl"

RUN mkdir /usr/src/golang
ENV GOPATH /usr/src/golang
ENV PATH $GOPATH/bin:$PATH

# Install Kepler dependencies
# TODO: install latest version of gradle
RUN apt-get update && apt-get install -y --no-install-recommends \
      build-essential \
      golang-go \
      libsodium-dev \
      mariadb-client \
      bash \
      crudini \
      openjdk-11-jdk-headless \
      gradle \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p "$GOPATH/src" "$GOPATH/bin" \
    && mkdir /usr/src/kepler \
    && groupadd -g 1000 kepler \
    && useradd -r -u 1000 -g kepler kepler \
    && chown -R kepler:kepler /usr/src/kepler \
    && go get -u github.com/amacneil/dbmate

COPY Kepler-Server/ /usr/src/kepler/Kepler-Server/
COPY tools/ /usr/src/kepler/tools/
COPY settings.gradle /usr/src/kepler

RUN cd /usr/src/kepler && \
    gradle build -x test fatJar && \
    cd

RUN touch /usr/src/kepler/game.ini && \
    touch /usr/src/kepler/server.ini && \
    crudini --set /usr/src/kepler/server.ini Server server.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/server.ini Server server.port 12321 && \
    crudini --set /usr/src/kepler/server.ini Rcon rcon.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/server.ini Rcon rcon.port 12309 && \
    crudini --set /usr/src/kepler/server.ini Mus mus.bind 0.0.0.0 && \
    crudini --set /usr/src/kepler/server.ini Mus mus.port 12322 && \
    crudini --set /usr/src/kepler/server.ini Database mysql.hostname mariadb && \
    crudini --set /usr/src/kepler/server.ini Database mysql.port 3306 && \
    crudini --set /usr/src/kepler/server.ini Database mysql.username kepler && \
    crudini --set /usr/src/kepler/server.ini Database mysql.password verysecret && \
    crudini --set /usr/src/kepler/server.ini Database mysql.database kepler && \
    crudini --set /usr/src/kepler/server.ini Logging log.connections true && \
    crudini --set /usr/src/kepler/server.ini Logging log.sent.packets false && \
    crudini --set /usr/src/kepler/server.ini Logging log.received.packets false && \
    crudini --set /usr/src/kepler/server.ini Console debug false && \
    cat /usr/src/kepler/server.ini && \
    cat /usr/src/kepler/game.ini && \
    chown kepler:kepler /usr/src/kepler/server.ini && \
    chown kepler:kepler /usr/src/kepler/game.ini

COPY docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]

WORKDIR /usr/src/kepler

EXPOSE 12321
EXPOSE 12309
EXPOSE 12322

USER kepler

CMD ["java", "-jar", "Kepler-Server/build/libs/Kepler-Server-all.jar"]
