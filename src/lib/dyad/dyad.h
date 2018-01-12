/**
 * Copyright (c) 2016 rxi
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MIT license. See LICENSE for details.
 */

#ifndef DYAD_H
#define DYAD_H

#include <stdarg.h>

#ifdef _WIN32
  #include <windows.h> /* For SOCKET */
#endif


#ifdef __cplusplus
extern "C" {
#endif

#if _WIN32
typedef SOCKET dyad_Socket;
#else
typedef int dyad_Socket;
#endif

static void *dyad_realloc(void *ptr, int n) {
    ptr = realloc(ptr, n);
    if (!ptr && n != 0) {
        //panic("out of memory");
    }
    return ptr;
}



/*===========================================================================*/
/* Vec (dynamic array)                                                       */
/*===========================================================================*/

static void vec_expand(char **data, int *length, int *capacity, int memsz) {
    if (*length + 1 > *capacity) {
        if (*capacity == 0) {
            *capacity = 1;
        } else {
            *capacity <<= 1;
        }
        *data = dyad_realloc(*data, *capacity * memsz);
    }
}

static void vec_splice(
        char **data, int *length, int *capacity, int memsz, int start, int count
) {
    (void) capacity;
    memmove(*data + start * memsz,
            *data + (start + count) * memsz,
            (*length - start - count) * memsz);
}


#define Vec(T)\
  struct { T *data; int length, capacity; }


#define vec_unpack(v)\
  (char**)&(v)->data, &(v)->length, &(v)->capacity, sizeof(*(v)->data)


#define vec_init(v)\
  memset((v), 0, sizeof(*(v)))


#define vec_deinit(v)\
  dyad_free((v)->data)


#define vec_clear(v)\
  ((v)->length = 0)


#define vec_push(v, val)\
  ( vec_expand(vec_unpack(v)),\
    (v)->data[(v)->length++] = (val) )


#define vec_splice(v, start, count)\
  ( vec_splice(vec_unpack(v), start, count),\
    (v)->length -= (count) )

struct dyad_Stream;

typedef struct player_s player;
typedef struct dyad_Stream dyad_Stream;

typedef struct {
  int type;
  void *udata;
  dyad_Stream *stream;
  dyad_Stream *remote;
  const char *msg;
  char *data;
  int size;
} dyad_Event;

typedef void (*dyad_Callback)(dyad_Event*);
typedef void (*dyad_PanicCallback)(const char*);

typedef struct {
    int event;
    dyad_Callback callback;
    void *udata;
} Listener;


struct dyad_Stream {
    int state, flags;
    dyad_Socket sockfd;
    char *address;
    int port;
    int bytesSent, bytesReceived;
    double lastActivity, timeout;
    Vec(Listener) listeners;
    Vec(char) lineBuffer;
    Vec(char) writeBuffer;
    dyad_Stream *next;
    player *player;
};




enum {
  DYAD_EVENT_NULL,
  DYAD_EVENT_DESTROY,
  DYAD_EVENT_ACCEPT,
  DYAD_EVENT_LISTEN,
  DYAD_EVENT_CONNECT,
  DYAD_EVENT_CLOSE,
  DYAD_EVENT_READY,
  DYAD_EVENT_DATA,
  DYAD_EVENT_LINE,
  DYAD_EVENT_ERROR,
  DYAD_EVENT_TIMEOUT,
  DYAD_EVENT_TICK
};

enum {
  DYAD_STATE_CLOSED,
  DYAD_STATE_CLOSING,
  DYAD_STATE_CONNECTING,
  DYAD_STATE_CONNECTED,
  DYAD_STATE_LISTENING
};


void dyad_init(void);
void dyad_update(void);
void dyad_shutdown(void);
const char *dyad_getVersion(void);
double dyad_getTime(void);
int  dyad_getStreamCount(void);
void dyad_setTickInterval(double seconds);
void dyad_setUpdateTimeout(double seconds);
dyad_PanicCallback dyad_atPanic(dyad_PanicCallback func);

dyad_Stream *dyad_newStream(void);
int  dyad_listen(dyad_Stream *stream, int port);
int  dyad_listenEx(dyad_Stream *stream, const char *host, int port,
                   int backlog);
int  dyad_connect(dyad_Stream *stream, const char *host, int port);
void dyad_addListener(dyad_Stream *stream, int event,
                      dyad_Callback callback, void *udata);
void dyad_removeListener(dyad_Stream *stream, int event,
                         dyad_Callback callback, void *udata);
void dyad_removeAllListeners(dyad_Stream *stream, int event);
void dyad_end(dyad_Stream *stream);
void dyad_close(dyad_Stream *stream);
void dyad_write(dyad_Stream *stream, const void *data, int size);
void dyad_vwritef(dyad_Stream *stream, const char *fmt, va_list args);
void dyad_writef(dyad_Stream *stream, const char *fmt, ...);
void dyad_setTimeout(dyad_Stream *stream, double seconds);
void dyad_setNoDelay(dyad_Stream *stream, int opt);
int  dyad_getState(dyad_Stream *stream);
const char *dyad_getAddress(dyad_Stream *stream);
int  dyad_getPort(dyad_Stream *stream);
int  dyad_getBytesSent(dyad_Stream *stream);
int  dyad_getBytesReceived(dyad_Stream *stream);
dyad_Socket dyad_getSocket(dyad_Stream *stream);

#ifdef __cplusplus
} // extern "C"
#endif

#endif
