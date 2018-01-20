#ifndef THREADING_H
#define THREADING_H

typedef struct deque_s Deque;
typedef struct runnable_s runnable;
typedef struct room_s room;
typedef void (*thread_request)(runnable *runnable);

struct thread_manager {
    Deque *tasks;
};

typedef struct runnable_s {
    room *room;
    thread_request request;
    runnable *self;
} runnable;

void create_thread_pool();
runnable *create_runnable();

#endif