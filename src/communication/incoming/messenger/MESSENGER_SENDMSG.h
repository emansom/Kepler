#include "list.h"

#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "game/player/player.h"
#include "database/queries/messenger_query.h"

void MESSENGER_SENDMSG(player *session, incoming_message *message) {
    int friend_count = im_read_vl64(message);

    printf("Debug 1 %i\n", friend_count);
    printf("Content: %s\n", im_get_content(message));

    List *friends;
    list_new(&friends);

    for (int i = 0; i < friend_count; i++) {
        int friend_id = im_read_vl64(message);
        list_add(friends, messenger_entry_create(friend_id));
        printf("Debug 2\n");
    }

    char *chat_message = im_read_str(message);

    /*for (int i = 0; i < list_size(friends); i++) {
        messenger_entry *friend;
        list_get_at(friends, i, (void*)&friend);

        print_info("sent to: %i\n", friend->friend_id);

        messenger_entry_cleanup(friend);
    }*/

    list_destroy(friends);
}