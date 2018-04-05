#include "shared.h"

#include "list.h"
#include "hashtable.h"

#include "database/queries/catalogue_query.h"

#include "catalogue_manager.h"
#include "catalogue_page.h"

void catalogue_manager_init() {
    hashtable_new(&global.catalogue_manager.pages);
    catalogue_query_pages();
}

void catalogue_add_page(catalogue_page *page) {
    hashtable_add(global.catalogue_manager.pages, &page->id, page);
}

catalogue_page *catalogue_manager_get_page_by_id(int page_id) {
    void *page = NULL;

    if (hashtable_contains_key(global.catalogue_manager.pages, &page_id)) {
        hashtable_get(global.catalogue_manager.pages, &page_id, (void *)&page);
    }

    return page;
}