#ifndef THREADING_H
#define THREADING_H

typedef struct deque_s Deque;
typedef struct runnable_s runnable;
typedef void (*thread_request)(void *argument, runnable *self);

struct thread_manager {
    Deque *tasks;
};

typedef struct runnable_s {
    void *argument;
    thread_request request;
} runnable;


void create_thread_pool();

#endif