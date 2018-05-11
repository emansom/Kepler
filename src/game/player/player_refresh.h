#ifndef PLAYER_REFRESH_H
#define PLAYER_REFRESH_H

typedef struct session_s session;

void player_send_localised_error(session *p, char *error);
void player_send_alert(session *p, char *text);
void player_refresh_credits(session *player);
void player_refresh_tickets(session *player);
void player_refresh_appearance(session *player);
void player_refresh_badges(session *player);

#endif