#ifndef THREADING_H
#define THREADING_H

typedef struct deque_s Deque;
typedef struct runnable_s runnable;
typedef struct room_s room;
typedef void (*thread_request)(room*);

struct thread_manager {
    Deque *tasks;
};

typedef struct runnable_s {
    thread_request request;
    runnable *self;
    int room_id;
} runnable;

void create_thread_pool();
int threading_has_room(int);
runnable *create_runnable();

#endif