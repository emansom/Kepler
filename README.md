## About

Kepler is a small TCP server written in C powered by libuv, an asynchronous networking library. It powers the Habbo Hotel version 13 client from 2006 era. The aim is to fully replicate this version by writing the back-end server in the C11 language.

## Compile Kepler

These commands will compile and run Kepler, first make sure you're in the directory of Kepler.

```
$ cmake .
$ make
$ ./Kepler
```

The CMake command is only necessary if you make changes to CMakeLists.txt, because it will rebuild the cache.

## Installation 

First, run these commands to ensure you have everything required to compile Kepler.

```
$ sudo apt-get install make cmake
$ sudo apt-get install git
$ sudo apt-get install automake
$ sudo apt-get install libtool
```

##### Installing libuv Library

Download the libuv source code.

```
$ git clone https://github.com/libuv/libuv.git
``` 

Enter the libuv directory.
```
$ cd libuv
```

Configure and compile it.
```
$ sh autogen.sh
$ ./configure
$ make
$ make check
$ make install
```

##### Installing the SQLite Library

Run these commands to install the SQLite library.

```
$ sudo apt-get install sqlite3 libsqlite3-dev
```

## Requirements

This server is only supported on Linux/POSIX systems. For Windows users, to use Kepler you must use the [Linux Subsystem for Windows](https://docs.microsoft.com/en-us/windows/wsl/install-win10) in order to run and compile the app in a Linux environment.