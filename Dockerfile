FROM ubuntu:18.04
LABEL maintainer="ewout@freedom.nl"

RUN apt update && apt install -y \
    make \
    cmake \
    libuv1-dev \
    sqlite3 \
    libsqlite3-dev \
    libsodium-dev \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir /usr/src/kepler

COPY src/ /usr/src/kepler/src/
COPY data/ /usr/src/kepler/data/
COPY CMakeLists.txt /usr/src/kepler
COPY kepler.sql /usr/src/kepler

RUN cd /usr/src/kepler && \
    rm -f CMakeCache.txt && \
    rm -f data/Kepler.db && \
    cmake . && \
    make && \
    mv Kepler /usr/bin/kepler && \
    cd

RUN useradd -r -u 1000 -U kepler && \
    chown -R kepler:kepler /usr/src/kepler

USER kepler
WORKDIR /usr/src/kepler

VOLUME /usr/src/Kepler/data

EXPOSE 12321
EXPOSE 12309

ENTRYPOINT ["kepler"]
