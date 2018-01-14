#include <stdio.h>
#include <stdlib.h>
#include "pthread.h"

#include "util/threading.h"

#define NUM_THREADS 5

pthread_t thr[NUM_THREADS];
thread_data thr_data[NUM_THREADS];

/**
 *
 * @param arg
 * @return
 */
void *thr_func(void *arg) {
	thread_data *data = (thread_data*)arg;
	pthread_exit(NULL);
}


/**
 *
 */
void create_thread_pool() {
	for (int i = 0; i < NUM_THREADS; ++i) {
		thr_data[i].tid = i;
		pthread_t thread;
		pthread_create(&thread, NULL, thr_func, &thr_data[i]);
	}
}