typedef struct coord_s coord;
typedef struct node_s node;
typedef struct deque_struct deque_type;

typedef struct pathfinder_s {
	node **map;
	node **open_list;
	node *current;
	node *nodes;
} pathfinder;

pathfinder *make_path_reversed(coord, coord, int, int);
int is_valid_tile(coord from, coord to);
void start_pathfinder_test();