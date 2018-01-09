#include "incoming_message.h"
/**
 * Creates an incoming message give by a char array
 * @param message the char array
 * @return incoming_message struct
 */
incoming_message *im_create(char *message) {
    incoming_message *im = malloc(sizeof(incoming_message));
    im->data = message;
    im->header = im_get_argument(im, " ", 0);
}

/**
 * Gets an argument from the incoming message by specified delimiters and position. Make sure to call
 * free() on these variables after they have been retrieved.
 *
 * @param im the incoming message
 * @param delimiter the delimeter to split by
 * @param position the position of the item to retrieve
 * @return
 */
char *im_get_argument(incoming_message *im, const char *delimiter, int position) {
    char message[1024];
    strcpy(message, im->data);

    char *token = strtok(message, delimiter);

    int i = 0;
    while (token != NULL) {
        if (i == position) {
            return token;
        }

        i++;
        token = strtok(NULL, delimiter);
    }

    return NULL;
}


/**
 * Call method to cleanup the incoming message
 * @param im the incoming message
 */
void im_cleanup(incoming_message *im) {
    free(im);
}