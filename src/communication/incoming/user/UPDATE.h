#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void UPDATE(session *player, incoming_message *message) {
    for (int i = 0; i < 3; i++) {
        int update_id = im_read_b64_int(message);

        if (update_id == 4) {
            char *content = im_read_str(message);
            filter_vulnerable_characters(&content, true);

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
                if (player->player_data->sex[0] != content[0]) { // Changed sex? Reset pool figure
                    free(player->player_data->pool_figure);
                    player->player_data->pool_figure = strdup("");
                }

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

    player_query_save_details(player);
    player_query_save_motto(player);
    GET_INFO(player, message);

    /*char *content;

    im_read_b64(message);
    content = im_read_str(message);

    if (content != NULL) {
        if (is_numeric(content)) {
            free(session->player_data->figure);
            session->player_data->figure = strdup(content);
            log_debug("figure: %s", session->player_data->figure);
        }

        free(content);
    }

    im_read_b64(message);
    content = im_read_str(message);

    if (content != NULL) {
        if ((content[0] == 'M' || content[0] == 'F') && strlen(content) == 1) {
            char *sex = strdup(content);

            free(session->player_data->sex);
            session->player_data->sex = sex;

            log_debug("sex updated");
        }

        free(content);
    }

    im_read_b64(message);
    content = im_read_str(message);

    if (content != NULL) {
        free(session->player_data->motto);
        session->player_data->motto = strdup(content);
        log_debug("motto: %s", session->player_data->motto);
        free(content);
    }*/
}
