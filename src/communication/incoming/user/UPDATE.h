#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void UPDATE(player *player, incoming_message *message) {
	char *content;

	im_read_b64(message);
	content = im_read_str(message);

	if (content != NULL) {
		if (is_numeric(content)) {
			free(player->player_data->figure);
			player->player_data->figure = strdup(content);
			printf("figure: %s\n", player->player_data->figure);
		}

		free(content);
	}

	im_read_b64(message);
	content = im_read_str(message);

	if (content != NULL) {
		if ((content[0] == 'M' || content[0] == 'F') && strlen(content) == 1) {
			char *sex = strdup(content);

			free(player->player_data->sex);
			player->player_data->sex = sex;

			printf("sex updated\n");
		}

		free(content);
	}

	im_read_b64(message);
	content = im_read_str(message);

	if (content != NULL) {
		char *motto = strdup(content);

		free(content);
	}
}
