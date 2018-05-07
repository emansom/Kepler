#include <unistd.h>

#include "list.h"
#include "log.h"

#include "game/player/player.h"
#include "game/room/room_user.h"
#include "game/room/manager/room_entity_manager.h"

#include "game_thread.h"
#include "shared.h"

void game_thread_init(pthread_t *thread) {
    if (pthread_create(thread, NULL, &game_thread_loop, NULL) != 0) {
        log_fatal("Uh-oh! Unable to spawn game thread");
    } else {
        log_info("Game thread successfully started!");
    }
}

void *game_thread_loop(void *arguments) {
    unsigned long tick = 0;

    while (!global.is_shutdown) {
        tick++;
        game_thread_task(tick);
        sleep(1);
    }

    printf("Game thread closed..\n");
    return NULL;
}

void game_thread_task(unsigned long ticks) {
    for (size_t i = 0; i < list_size(global.player_manager.players); i++) {
        session *player;
        list_get_at(global.player_manager.players, i, (void *) &player);

        if (player->disconnected) {
            continue;
        }

        bool disposed = false;

        // Check ping timeout
        if (ticks % 60 == 0) {
            if (player->ping_safe) {
                player->ping_safe = false;

                outgoing_message *om = om_create(50); // "@r"
                player_send(player, om);
                om_cleanup(om);
            } else {
                if (player->logged_in) {
                    log_info("Player %s timed out", player->player_data->username);
                } else {
                    log_info("Connection %s timed out", player->ip_address);
                }

                player_cleanup(player);
                disposed = true;
            }
        }

        if (disposed) {
            continue;
        }

        if (player->logged_in) {
            if (player->room_user->room != NULL && time(NULL) > player->room_user->room_idle_timer) {
                room_leave(player->room_user->room, player, true); // Kick and send to hotel view
            }
        }
    }
}