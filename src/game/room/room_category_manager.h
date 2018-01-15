#ifndef ROOM_CATEGORY_MANAGER_H
#define ROOM_CATEGORY_MANAGER_H

typedef struct list_s List;

struct room_category_manager {
    List *categories;
};

typedef struct room_category_s {
    int id;
    int parent_id;
    char *name;
    int public_spaces;
    int allow_trading;
    int category_type;
} room_category;

void category_manager_init();
room_category *category_manager_create(int, int, char*, int, int);
void category_manager_add(room_category*);
room_category *category_manager_get_by_id(int);
List *category_manager_get_by_parent_id(int);

#endif