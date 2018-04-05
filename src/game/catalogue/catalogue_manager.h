#ifndef CATALOGUE_MANAGER_H
#define CATALOGUE_MANAGER_H

typedef struct hashtable_s HashTable;
typedef struct catalogue_page_s catalogue_page;

struct catalogue_manager {
    HashTable *pages;
};

void catalogue_manager_init();
void catalogue_add_page(catalogue_page*);
catalogue_page *catalogue_manager_get_page_by_id(int);

#endif