#ifndef ITEM_BEHAVIOUR_H
#define ITEM_BEHAVIOUR_H

#include <stdbool.h>

typedef struct item_definition_s item_definition;

typedef struct item_behaviour_s {
    bool is_wall_item;
    bool is_solid;
    bool can_sit_on_top;
    bool can_lay_on_top;
    bool can_stand_on_top;
    bool canStackOnTop;
    bool isRoller;
    bool is_public_space_object;
    bool isInvisible;
    bool requiresRightsForInteraction;
    bool requiresTouchingForInteraction;
    bool customDataTrueFalse;
    bool customDataOnOff;
    bool customDataNumericOnOff;
    bool customDataNumericState;
    bool is_decoration;
    bool isPostIt;
    bool isDoor;
    bool isTeleporter;
    bool isDice;
    bool is_prize_trophy;
    bool isRedeemable;
    bool isSoundMachine;
    bool isSoundMachineSampleSet;
    bool has_extra_parameter;
} item_behaviour;

item_behaviour *item_behaviour_create();
item_behaviour *item_behaviour_parse(item_definition *def);

#endif