#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void UPDATE(player *player, incoming_message *message) {
	for (int i = 0; i < 3; i++) {
		int update_id = im_read_b64_int(message);

		if (update_id == 4) {
			char *content = im_read_str(message);
			
			if (content != NULL) {
				if (is_numeric(content) && strlen(content) == 25) {
					free(player->player_data->figure);
					player->player_data->figure = content;
				} else {
					free(content);
				}
			}
		}

		if (update_id == 5) {
			char *content = im_read_str(message);

			if (content != NULL) {
				if ((content[0] == 'M' || content[0] == 'F') && strlen(content) == 1) {
					free(player->player_data->sex);
					player->player_data->sex = content;
				} else {
					free(content);
				}
			}
		}

		if (update_id == 6) {
			char *content = im_read_str(message);

			if (content != NULL) {
				free(player->player_data->motto);
				player->player_data->motto = content;
			}
		}
	}

	query_player_save(player);
	GET_INFO(player, message);

	/*char *content;

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
		free(player->player_data->motto);
		player->player_data->motto = strdup(content);
		printf("motto: %s\n", player->player_data->motto);
		free(content);
	}*/
}
