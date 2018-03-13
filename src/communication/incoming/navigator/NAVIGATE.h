#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/navigator/navigator_category.h"
#include "game/navigator/navigator_category_manager.h"

#include "game/room/room.h"

#include "list.h"

void NAVIGATE(player *player, incoming_message *message) {
    int hide_full = im_read_vl64(message);
    int category_id = im_read_vl64(message);

    room_category *parent_category = category_manager_get_by_id(category_id);
    outgoing_message *navigator = om_create(220); // "C\"

    if (parent_category != NULL && category_has_access(parent_category, player->player_data->rank)) {
        om_write_int(navigator, hide_full);
        om_write_int(navigator, category_id);

        if (parent_category->category_type == PUBLIC) {
            om_write_int(navigator, 0);
        } else {
            om_write_int(navigator, 2);
        }
        om_write_str(navigator, parent_category->name);
        om_write_int(navigator, category_manager_get_current_vistors(category_id)); // current visitors
        om_write_int(navigator, category_manager_get_max_vistors(category_id)); // max visitorss
        om_write_int(navigator, parent_category->parent_id); 

        List *rooms = category_manager_get_rooms(parent_category->id);

        if (parent_category->category_type == PRIVATE) {
            om_write_int(navigator, list_size(rooms));  // room count
        }

        for (int i = 0; i < list_size(rooms); i++) {
            room *instance;
            list_get_at(rooms, i, (void*)&instance);
            
            category_serialise(navigator, instance, parent_category->category_type, player);
        }

        List *child_categories = category_manager_get_by_parent_id(parent_category->id);

        for (int i = 0; i < list_size(child_categories); i++) {
            room_category *category;
            list_get_at(child_categories, i, (void*)&category);

            int current_visitors = category_manager_get_current_vistors(category->id);
            int max_visitors = category_manager_get_max_vistors(category->id);

            if (category_has_access(category, player->player_data->rank) && (!hide_full || current_visitors < max_visitors)) {
                om_write_int(navigator, category->id);
                om_write_int(navigator, 0);
                om_write_str(navigator, category->name);
                om_write_int(navigator, current_visitors);
                om_write_int(navigator, max_visitors); 
                om_write_int(navigator, parent_category->id); 
            }
        }

        list_destroy(rooms);
        list_destroy(child_categories);
    }
        
    player_send(player, navigator);
    om_cleanup(navigator);
}
