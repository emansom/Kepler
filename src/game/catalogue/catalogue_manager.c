#include "shared.h"

#include "list.h"
#include "hashtable.h"

#include "database/queries/catalogue_query.h"

#include "catalogue_manager.h"
#include "catalogue_page.h"

/**
 * Create the catalogue manager instance and load the pages.
 */
void catalogue_manager_init() {
    list_new(&global.catalogue_manager.pages);
    catalogue_query_pages();
}

/**
 * Add a page by it's given catalogue page struct.
 *
 * @param page the catalogue page struct
 */
void catalogue_manager_add_page(catalogue_page *page) {
    list_add(global.catalogue_manager.pages, page);
}

/**
 * Get the catalogue page by it's requested catalogue index.
 *
 * @param page_index the page index
 * @return the catalogue page struct
 */
catalogue_page *catalogue_manager_get_page_by_index(char *page_index) {
    for (size_t i = 0; i < list_size(global.catalogue_manager.pages); i++) {
        catalogue_page *search_page = NULL;
        list_get_at(global.catalogue_manager.pages, i, (void *) &search_page);

        if (strcmp(search_page->name_index, page_index) == 0) {
            return search_page;
        }
    }

    return NULL;
}

/**
 * Get the entire list of catalogue pages
 *
 * @return list of pages
 */
List *catalogue_manager_get_pages() {
    return global.catalogue_manager.pages;
}


/**
 * Dispose model manager
 */
void catalogue_manager_dispose() {
    for (size_t i = 0; i < list_size(global.catalogue_manager.pages); i++) {
        catalogue_page *page = NULL;
        list_get_at(global.catalogue_manager.pages, i, (void *) &page);
        catalogue_page_dispose(page);
    }

    list_destroy(global.catalogue_manager.pages);
}