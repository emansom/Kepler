#ifndef CATALOGUE_MANAGER_H
#define CATALOGUE_MANAGER_H

typedef struct hashtable_s HashTable;
typedef struct catalogue_page_s catalogue_page;
typedef struct catalogue_item_s catalogue_item;

struct catalogue_manager {
    HashTable *pages;
    HashTable *items;
};

void catalogue_manager_init();
void catalogue_manager_add_page(catalogue_page *page);
void catalogue_manager_add_item(catalogue_item *item);
catalogue_page *catalogue_manager_get_page_by_id(int id);
catalogue_page *catalogue_manager_get_page_by_index(char *index);
HashTable *catalogue_manager_get_pages();
void catalogue_manager_dispose();

#endif