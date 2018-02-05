#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void UPDATE_ACCOUNT(player *player, incoming_message *message) {
	im_read_b64_int(message);
	char *password = im_read_str(message);

	im_read_b64_int(message);
	char *date_of_birth = im_read_str(message);

	im_read_b64_int(message);
	char *new_password = im_read_str(message);

	if (password == NULL || date_of_birth == NULL || new_password == NULL) {
		goto cleanup;
		return;
	}

	int error_id = 0;
	
	outgoing_message *om = om_create(169); // "Bi"
	
	

	cleanup:
		if (password != NULL) {
			free(password);
		}

		if (date_of_birth != NULL) {
			free(date_of_birth);
		}
			
		if (password != NULL) {
			free(password);
		}
}
