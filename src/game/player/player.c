#include <stdio.h>

#include "player.h"
#include "dyad.h"

#include "sqlite3.h"

#include "util/stringbuilder.h"
#include "communication/messages/outgoing_message.h"

/**
 * Creates a new player
 * @return player struct
 */
player *player_create(dyad_Stream *stream) {
    player *player = malloc(sizeof(player));
    player->stream = stream;
    player->player_data = NULL;
    return player;
}

/**
 * Creates a new player data instance
 * @return player data struct
 */
player_data *player_create_data(int id, char *username, char *figure, int credits, char *motto, char *sex, int tickets, int film) {
    player_data *player_data = malloc(sizeof(player_data));
    player_data->id = id;
    player_data->username = strdup(username);
    player_data->figure = strdup(figure);
    player_data->credits = credits;
    player_data->motto = strdup(motto);
    player_data->sex = strdup(sex);
    player_data->tickets = tickets;
    player_data->film = film;/*

    player_data->username[strlen(username)] = '\0';
    player_data->figure[strlen(figure)] = '\0';
    player_data->motto[strlen(motto)] = '\0';
    player_data->sex[strlen(sex)] = '\0';*/
    return player_data;
}

/**
 * Called when a connection is successfully opened
 * @param p the player struct
 */
void player_init(player *p) {
    printf("Player initialise %s\n", dyad_getAddress(p->stream));
}

/**
 * Called when a connection is closed
 * @param p the player struct
 */
void player_cleanup(player *p) {
    player *player = p;
    printf("Player cleanup %s\n", dyad_getAddress(p->stream));

    /*if (p->player_data != NULL) {
        sqlite3_free (p->player_data->username);
        sqlite3_free (p->player_data->figure);
        sqlite3_free (p->player_data->motto);
        sqlite3_free (p->player_data->sex);
        free (p->player_data);
    }*/

    //free(player);
}

/**
 * Send an outgoing message to the socket
 * @param p the player struct
 * @param om the outgoing message
 */
void player_send(player *p, outgoing_message *om) {
    om_finalise(om);

    char *data = om->sb->data;
    dyad_write(p->stream, data, strlen(data));

    printf ("Client [%s] outgoing data: %i / %s\n", dyad_getAddress(p->stream), om->header_id, data);
}

/**
 * Sends the key of an error, whose description value is inside the external_texts of the client.
 * @param p the player
 * @param error the error message
 */
void send_localised_error(player *p, char *error) {
    outgoing_message *om = om_create(33); // @a
    om_write_str(om, error);

    player_send(p, om);
    om_cleanup(om);
}

/**
 * Send an alert to the player
 * @param p the player
 * @param greeting the alert message
 */
void send_alert(player *p, char *greeting) {
    outgoing_message *welcome_message = om_create(139); // BK
    om_write_str(welcome_message, greeting);
    player_send(p, welcome_message);
    om_cleanup(welcome_message);
}