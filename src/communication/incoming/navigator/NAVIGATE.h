#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/room/room_category_manager.h"

#include "list.h"

void NAVIGATE(player *player, incoming_message *message) {
    int hide_full = im_read_vl64(message);
    int category_id = im_read_vl64(message);

    room_category *parent_category = category_manager_get_by_id(category_id);
    outgoing_message *navigator = om_create(220); // "C\"

    if (parent_category != NULL) {
        om_write_int(navigator, hide_full);
        om_write_int(navigator, category_id);
        om_write_int(navigator, parent_category->category_type); // current visitors
        om_write_str(navigator, parent_category->name);
        om_write_int(navigator, 0); // current visitors
        om_write_int(navigator, 25); // max visitorss
        om_write_int(navigator, parent_category->parent_id); 

        if (parent_category->public_spaces == 0) {
            om_write_int(navigator, 0);  // room count
        }

        List *child_categories = category_manager_get_by_parent_id(parent_category->id);
        ListIter iter;

        list_iter_init(&iter, child_categories);
        room_category *category;

        while (list_iter_next(&iter, (void*) &category) != CC_ITER_END) {
            om_write_int(navigator, category->id);
            om_write_int(navigator, 0);//category->category_type); // current visitors
            om_write_str(navigator, category->name);
            om_write_int(navigator, 0); // current visitors
            om_write_int(navigator, 25); // max visitorss
            om_write_int(navigator, parent_category->id); 
        }

        list_destroy(child_categories);
    }
        
    player_send(player, navigator);
    om_cleanup(navigator);
}
