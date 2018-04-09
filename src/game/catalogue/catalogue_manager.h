#ifndef CATALOGUE_MANAGER_H
#define CATALOGUE_MANAGER_H

typedef struct list_s List;
typedef struct catalogue_page_s catalogue_page;

struct catalogue_manager {
    List *pages;
};

void catalogue_manager_init();
void catalogue_manager_add_page(catalogue_page*);
catalogue_page *catalogue_manager_get_page_by_index(char*);
List *catalogue_manager_get_pages();
void catalogue_manager_dispose();

#endif