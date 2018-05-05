FROM ubuntu:18.04

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
    cmake . && \
    make && \
    mv Kepler /usr/bin/kepler && \
    cd

WORKDIR /usr/src/kepler

EXPOSE 12321
ENTRYPOINT ["kepler"]