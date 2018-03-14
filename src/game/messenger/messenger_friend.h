#ifndef MESSENGER_FRIEND_H
#define MESSENGER_FRIEND_H

typedef struct outgoing_message_s outgoing_message;

typedef struct messenger_friend_s {
    int friend_id;
} messenger_friend;

messenger_friend *messenger_friend_create(int friend_id);
void messenger_friend_cleanup(messenger_friend*);
void messenger_serialise(int, outgoing_message*);

#endif