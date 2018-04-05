#ifndef CATALOGUE_PAGE_H
#define CATALOGUE_PAGE_H

typedef struct catalogue_page_s {
    int id;
    int parent_id;
    char *name;
} catalogue_page;

catalogue_page *catalogue_page_create();

#endif