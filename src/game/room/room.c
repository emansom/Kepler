#include <stdlib.h>
#include "room.h"

room *room_create(int room_id) {
    room *instance = malloc(sizeof(room));
    instance->room_id = room_id;
    instance->room_data = NULL;
    return instance;
}