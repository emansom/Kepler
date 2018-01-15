#include "shared.h"

#include "list.h"

#include "database/queries/room_query.h"
#include "room_category_manager.h"

void category_manager_init() {
    list_new(&global.room_category_manager.categories);
    room_query_get_categories();
}

room_category *category_manager_create(int id, int parent_id, char *name, int public_spaces, int allow_trading) {
    room_category *category = malloc(sizeof(room_category));
    category->id = id;
    category->parent_id = parent_id;
    category->name = strdup(name);
    category->public_spaces = public_spaces;
    category->allow_trading = allow_trading;

    if (public_spaces == 0) {
        category->category_type = 2;
    } else {
        category->category_type = 0;
    }

    return category;
}

void category_manager_add(room_category *category) {
    list_add(global.room_category_manager.categories, category);
}

room_category *category_manager_get_by_id(int category_id) {
    ListIter iter;
    list_iter_init(&iter, global.room_category_manager.categories);

    room_category *category;

    while (list_iter_next(&iter, (void*) &category) != CC_ITER_END) {
        if (category->id == category_id) {
            return category;
        }
    }

    return NULL;
}

List *category_manager_get_by_parent_id(int category_id) {
    List *sub_categories;
    list_new(&sub_categories);

    ListIter iter;

    list_iter_init(&iter, global.room_category_manager.categories);
    room_category *category;

    while (list_iter_next(&iter, (void*) &category) != CC_ITER_END) {
        if (category->parent_id == category_id) {
            printf("sub cat: %s\n", category->name);
            list_add(sub_categories, category);
        }
    }

    return sub_categories;
}