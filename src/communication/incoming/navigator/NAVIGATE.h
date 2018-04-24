#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/navigator/navigator_category.h"
#include "game/navigator/navigator_category_manager.h"

#include "game/room/room.h"

#include "list.h"

#include "database/queries/room_query.h"

void NAVIGATE(session *player, incoming_message *message) {
    bool hide_full = (bool) im_read_vl64(message);
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
        List *recent_rooms = NULL;

        list_sort_in_place(rooms, room_manager_sort);

        if (parent_category->category_type == PRIVATE) {
             recent_rooms = room_query_recent_rooms(50, parent_category->id);

            for (size_t i = 0; i < list_size(recent_rooms); i++) {
                room *instance;
                list_get_at(recent_rooms, i, (void *) &instance);

                if (room_manager_get_by_id(instance->room_id) == NULL) {
                    list_add(rooms, instance);
                }
            }

            om_write_int(navigator, (int)list_size(rooms));  // room count
        }

        for (size_t i = 0; i < list_size(rooms); i++) {
            room *instance;
            list_get_at(rooms, i, (void *) &instance);

            if (parent_category->category_type == PUBLIC) {
                om_write_int(navigator, instance->room_data->id); // room id
                om_write_int(navigator, 1);
                om_write_str(navigator, instance->room_data->name);
                om_write_int(navigator, instance->room_data->visitors_now); // current visitors
                om_write_int(navigator, instance->room_data->visitors_max); // max vistors
                om_write_int(navigator, instance->room_data->category); // category id
                om_write_str(navigator, instance->room_data->description); // description
                om_write_int(navigator, instance->room_data->id); // room id
                om_write_int(navigator, 0);
                om_write_str(navigator, instance->room_data->ccts);
                om_write_int(navigator, 0);
                om_write_int(navigator, 1);
            }

            if (parent_category->category_type == PRIVATE) {
                om_write_int(navigator, instance->room_data->id); // room id
                om_write_str(navigator, instance->room_data->name);

                if (player->player_data->id == instance->room_data->owner_id || instance->room_data->show_name == 1) {
                    om_write_str(navigator, instance->room_data->owner_name); // room owner
                } else {
                    om_write_str(navigator, "-"); // room owner
                }

                if (instance->room_data->accesstype == 2) {
                    om_write_str(navigator, "password");
                }

                if (instance->room_data->accesstype == 1) {
                    om_write_str(navigator, "closed");
                }

                if (instance->room_data->accesstype == 0) {
                    om_write_str(navigator, "open");
                }

                om_write_int(navigator, instance->room_data->visitors_now); // current visitors
                om_write_int(navigator, instance->room_data->visitors_max); // max vistors
                om_write_str(navigator, instance->room_data->description); // description
            }
        }

        List *child_categories = category_manager_get_by_parent_id(parent_category->id);

        /* Navigator.Append(
         * Encoding.encodeVL64(subCataIDs[i]) +
         * Encoding.encodeVL64(0) +
         * subName + Convert.ToChar(2) +
         * Encoding.encodeVL64(visitorCount) +
         * Encoding.encodeVL64(visitorMax) +
         * Encoding.encodeVL64(cataID));
                                    }*/

        for (size_t i = 0; i < list_size(child_categories); i++) {
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

        if (recent_rooms != NULL) {
            for (size_t i = 0; i < list_size(recent_rooms); i++) {
                room *instance;
                list_get_at(recent_rooms, i, (void *) &instance);

                printf("disposed %i\n", instance->room_id);

                room_dispose(instance);
            }

            list_destroy(recent_rooms);
        }

        list_destroy(rooms);
        list_destroy(child_categories);
    }
        
    session_send(player, navigator);
    om_cleanup(navigator);
}
