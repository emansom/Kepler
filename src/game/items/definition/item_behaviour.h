#ifndef ITEM_BEHAVIOUR_H
#define ITEM_BEHAVIOUR_H

#include <stdbool.h>

typedef struct item_definition_s item_definition;

typedef struct item_behaviour_s {
    bool isWallItem;
    bool isSolid;
    bool canSitOnTop;
    bool canLayOnTop;
    bool canStandOnTop;
    bool canStackOnTop;
    bool isRoller;
    bool isPublicSpaceObject;
    bool isInvisible;
    bool requiresRightsForInteraction;
    bool requiresTouchingForInteraction;
    bool customDataTrueFalse;
    bool customDataOnOff;
    bool customDataNumericOnOff;
    bool customDataNumericState;
    bool isDecoration;
    bool isPostIt;
    bool isDoor;
    bool isTeleporter;
    bool isDice;
    bool isPrizeTrophy;
    bool isRedeemable;
    bool isSoundMachine;
    bool isSoundMachineSampleSet;
} item_behaviour;

item_behaviour *item_behaviour_create();
item_behaviour *item_behaviour_parse(item_definition *def);

#endif