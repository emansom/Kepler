#include "shared.h"

#include "list.h"
#include "hashtable.h"

#include "database/queries/catalogue_query.h"

#include "catalogue_manager.h"
#include "catalogue_page.h"

void catalogue_manager_init() {
    list_new(&global.catalogue_manager.pages);
    catalogue_query_pages();
}

void catalogue_manager_add_page(catalogue_page *page) {
    list_add(global.catalogue_manager.pages, page);
}

catalogue_page *catalogue_manager_get_page_by_index(char *page_index) {
    void *page = NULL;

    for (size_t i = 0; i < list_size(global.catalogue_manager.pages); i++) {
        catalogue_page *search_page = NULL;
        list_get_at(global.catalogue_manager.pages, i, (void *) &search_page);

        if (search_page->name_index == page_index) {
            return page;
        }
    }

    return NULL;
}

List *catalogue_manager_get_pages() {
    return global.catalogue_manager.pages;
}