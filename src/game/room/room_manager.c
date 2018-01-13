#include "room_manager.h"
#include "hashtable.h"

#include "shared.h"

/**
 * Create a new hashtable to store players
 */
void room_manager_init() {
    hashtable_new(&global.player_manager.players);
}