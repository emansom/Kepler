#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/pathfinder/coord.h"
#include "game/player/player.h"

#include "game/room/room.h"
#include "game/room/room_user.h"

#include "list.h"

void CHAT(player *session, incoming_message *im) {
    if (session->room_user->room == NULL) {
        return;
    }
    
    char *message = im_read_str(im);
    room *room = session->room_user->room;

    int source_x = session->room_user->current->x;
    int source_y = session->room_user->current->y;
    
    outgoing_message *status_update = om_create(34);

    for (int i = 0; i < list_size(room->users); i++) {
        player *room_player;
        list_get_at(room->users, i, (void*)&room_player);

        int dist_x = abs(source_x - room_player->room_user->current->x) - 1;
        int dist_y = abs(source_y - room_player->room_user->current->y) - 1;

        outgoing_message *om = om_create(24); // "@X"
        om_write_int(om, session->player_data->id);

        if (dist_x < 9 && dist_y < 9) {// User can hear
            if (dist_x <= 6 && dist_y <= 6) {// User can hear full message
                om_write_str(om, message);
            } else {
                int garble_intensity = dist_x;

                if (dist_y < dist_x) {
                    garble_intensity = dist_y;
                }
                
                garble_intensity -= 4;
                char *garble_message = strdup(message);

                for (int pos = 0; pos < strlen(garble_message); pos++) {
                    int intensity = ((rand() & 7) + garble_intensity);

                    if (intensity > 3 && 
                        garble_message[pos] != ' ' &&  
                        garble_message[pos] != ',' && 
                        garble_message[pos] != '?' && 
                        garble_message[pos] != '!') {
                        garble_message[pos] = '.';
                    }
                }

                om_write_str(om, garble_message);
                free(garble_message);
            }

            player_send(room_player, om);
            om_cleanup(om);
        }
    }

    free(message);
}
