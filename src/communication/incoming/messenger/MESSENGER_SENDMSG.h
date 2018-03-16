#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "database/queries/messenger_query.h"

void MESSENGER_SENDMSG(player *session, incoming_message *message) {
    int friend_count = im_read_vl64(message);
    List *friends;
    list_new(&friends);

    for (int i = 0; i < friend_count; i++) {
        int friend_id = im_read_vl64(message);
        list_add(friends, messenger_entry_create(friend_id));
    }

    char *chat_message = im_read_str(message);
    filter_vulnerable_characters(&chat_message, 0);

    for (int i = 0; i < list_size(friends); i++) {
        messenger_entry *friend;
        list_get_at(friends, i, (void*)&friend);

        char *date = get_time_formatted();

        int message_id = messenger_query_new_message(friend->friend_id, session->player_data->id, chat_message, date);
        player *player_friend = player_manager_find_by_id(friend->friend_id);

        if (player_friend != NULL) {
            outgoing_message *response = om_create(134); // "BF"
            om_write_int(response, 1);
            om_write_int(response, message_id);
            om_write_int(response, session->player_data->id);
            om_write_str(response, "dd-MM-yyyy HH:mm:ss");
            om_write_str(response, chat_message);
            player_send(player_friend, response);
            om_cleanup(response);
        }

        messenger_entry_cleanup(friend);
        free(date);
    }

    list_destroy(friends);
}