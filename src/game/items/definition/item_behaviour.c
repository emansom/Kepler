#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "item_behaviour.h"
#include "item_definition.h"

item_behaviour *item_behaviour_create() {
    item_behaviour *behaviour = malloc(sizeof(item_behaviour));
    behaviour->isWallItem = false;
    behaviour->isSolid = false;
    behaviour->canSitOnTop = false;
    behaviour->canLayOnTop = false;
    behaviour->canStandOnTop = false;
    behaviour->canStackOnTop = false;
    behaviour->isRoller = false;
    behaviour->isPublicSpaceObject = false;
    behaviour->isInvisible = false;
    behaviour->requiresRightsForInteraction = false;
    behaviour->requiresTouchingForInteraction = false;
    behaviour->customDataTrueFalse = false;
    behaviour->customDataOnOff = false;
    behaviour->customDataNumericOnOff = false;
    behaviour->customDataNumericState = false;
    behaviour->isDecoration = false;
    behaviour->isPostIt = false;
    behaviour->isDoor = false;
    behaviour->isTeleporter = false;
    behaviour->isDice = false;
    behaviour->isPrizeTrophy = false;
    behaviour->isRedeemable = false;
    behaviour->isSoundMachine = false;
    behaviour->isSoundMachineSampleSet = false;
    behaviour->hasExtraParameter = false;
    return behaviour;
}

item_behaviour *item_behaviour_parse(item_definition *def) {
    item_behaviour *behaviour = item_behaviour_create();
    
    for (int i = 0; i < strlen(def->behaviour_data); i++) {
        char c = def->behaviour_data[i];

        if (c == 'W') {
            behaviour->isWallItem = true;
        }

        if (c == 'S') {
            behaviour->isSolid = true;
        }

        if (c == 'C') {
            behaviour->canSitOnTop = true;
        }

        if (c == 'B') {
            behaviour->canLayOnTop = true;
        }

        if (c == 'K') {
            behaviour->canStandOnTop = true;
        }

        if (c == 'R') {
            behaviour->isRoller = true;
        }

        if (c == 'P') {
            behaviour->isPublicSpaceObject = true;
        }

        if (c == 'I') {
            behaviour->isInvisible = true;
        }

        if (c == 'G') {
            behaviour->requiresRightsForInteraction = true;
        }

        if (c == 'T') {
            behaviour->requiresTouchingForInteraction = true;
        }

        if (c == 'U') {
            behaviour->customDataTrueFalse = true;
        }

        if (c == 'O') {
            behaviour->customDataOnOff = true;
        }

        if (c == 'M') {
            behaviour->customDataNumericOnOff = true;
        }

        if (c == 'Z') {
            behaviour->customDataNumericState = true;
        }

        if (c == 'H') {
            behaviour->canStackOnTop = true;
        }

        if (c == 'V') {
            behaviour->isDecoration = true;
        }

        if (c == 'J') {
            behaviour->isPostIt = true;
        }

        if (c == 'D') {
            behaviour->isDoor = true;
        }

        if (c == 'X') {
            behaviour->isTeleporter = true;
        }

        if (c == 'F') {
            behaviour->isDice = true;
        }

        if (c == 'Y') {
            behaviour->isPrizeTrophy = true;
        }

        if (c == 'Q') {
            behaviour->isRedeemable = true;
        }

        if (c == 'A') {
            behaviour->isSoundMachine = true;
        }

        if (c == 'N') {
            behaviour->isSoundMachineSampleSet = true;
        }
    }

    return behaviour;
}

