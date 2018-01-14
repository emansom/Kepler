#ifndef THREADING_H
#define THREADING_H

typedef struct thread_data_s {
  int tid;
  double stuff;
} thread_data;

void create_thread_pool();

#endif