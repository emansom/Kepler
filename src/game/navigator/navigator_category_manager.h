#ifndef NAVIGATOR_CATEGORY_MANAGER_H
#define NAVIGATOR_CATEGORY_MANAGER_H

typedef struct list_s List;
typedef struct outgoing_message_s outgoing_message;
typedef struct room_s room;

struct room_category_manager {
    List *categories;
};

typedef enum {
    PUBLIC,
    PRIVATE
} room_category_type;

typedef struct room_category_s {
    int id;
    int parent_id;
    char *name;
    int public_spaces;
    int allow_trading;
    room_category_type category_type;
} room_category;

void category_manager_init();
room_category *category_manager_create(int, int, char*, int, int);
void category_manager_add(room_category*);
room_category *category_manager_get_by_id(int);
List *category_manager_get_by_parent_id(int);
List *category_manager_get_rooms(int);
void category_manager_serialise(outgoing_message*, room*, room_category_type);

#endif