#ifndef THREADING_H
#define THREADING_H

typedef struct hashtable_s HashTable;
typedef struct runnable_s runnable;
typedef struct room_s room;
typedef struct thpool_* threadpool;

typedef void (*thread_request)(room*);

struct thread_manager {
    HashTable *tasks;
    threadpool pool;
};

typedef struct runnable_s {
    thread_request request;
    runnable *self;
    int room_id;
} runnable;

void do_room_task(runnable*);
void create_thread_pool();
int threading_has_room(int);
runnable *create_runnable();

#endif