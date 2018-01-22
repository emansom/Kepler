#ifndef PATHFINDER_H
#define PATHFINDER_H

typedef struct room_user_s room_user;
typedef struct node_s node;
typedef struct deque_s Deque;
typedef struct coord_s coord;

typedef struct pathfinder_s {
	node *map[200][200];
	Deque *open_list;
	node *current;
	node *nodes;
} pathfinder;

Deque *create_path(room_user*);
pathfinder *make_path_reversed(room_user*, int, int);
int is_valid_tile(room_user*, coord from, coord to, int is_final_move);

#endif