#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void UPDATE(player *player, incoming_message *message) {
	char *content;

	im_read_b64(message);
	content = im_read_str(message);

	if (content != NULL) {
		printf("figure: %s\n", content);
		free(content);
	}

	im_read_b64(message);
	content = im_read_str(message);

	if (content != NULL) {
		printf("figure: %s\n", content);
		free(content);
	}

	im_read_b64(message);
	content = im_read_str(message);

	if (content != NULL) {
		printf("figure: %s\n", content);
		free(content);
	}
}
