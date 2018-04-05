#include <stdlib.h>
#include <string.h>

#include "communication/messages/outgoing_message.h"
#include "game/player/player.h"

#include "catalogue_page.h"

/**
 *
 * @param id
 * @param min_role
 * @param name_index
 * @param name
 * @param layout
 * @param image_headline
 * @param image_teasers
 * @param body
 * @param label_pick
 * @param label_extra_s
 * @param label_extra_t
 * @return
 */
catalogue_page *catalogue_page_create(int id, int min_role, char *name_index, char *name, char *layout, char *image_headline, char *image_teasers, char *body, char *label_pick, char *label_extra_s, char *label_extra_t) {
    catalogue_page *page = malloc(sizeof(catalogue_page));
    page->id = id;
    page->min_role = min_role;
    page->name_index = name_index;
    page->name = name;
    page->layout = layout;
    page->image_headline = image_headline;
    page->image_teasers = image_teasers;
    page->body = body;
    page->label_pick = label_pick;
    page->label_extra_s = label_extra_s;
    page->label_extra_t = label_extra_t;
    return page;
}