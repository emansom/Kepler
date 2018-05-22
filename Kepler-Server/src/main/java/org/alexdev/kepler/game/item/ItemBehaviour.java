package org.alexdev.kepler.game.item;

public class ItemBehaviour {
    private boolean isWallItem;
    private boolean isSolid;
    private boolean canSitOnTop;
    private boolean canLayOnTop;
    private boolean canStandOnTop;
    private boolean canStackOnTop;
    private boolean isRoller;
    private boolean isPublicSpaceObject;
    private boolean isInvisible;
    private boolean requiresRightsForInteraction;
    private boolean requiresTouchingForInteraction;
    private boolean customDataTrueFalse;
    private boolean customDataOnOff;
    private boolean customDataNumericOnOff;
    private boolean customDataNumericState;
    private boolean isDecoration;
    private boolean isPostIt;
    private boolean isDoor;
    private boolean isTeleporter;
    private boolean isDice;
    private boolean isPrizeTrophy;
    private boolean isRedeemable;
    private boolean isSoundMachine;
    private boolean isSoundMachineSampleSet;
    private boolean has_extra_parameter;

    public static ItemBehaviour parse(String behaviourData) {
        ItemBehaviour behaviour = new ItemBehaviour();

        for (int i = 0; i < behaviourData.length(); i++) {
            char c = behaviourData.charAt(i);

            if (c == 'W') {
                behaviour.isWallItem = true;
            }

            if (c == 'S') {
                behaviour.isSolid = true;
            }

            if (c == 'C') {
                behaviour.canSitOnTop = true;
            }

            if (c == 'B') {
                behaviour.canLayOnTop = true;
            }

            if (c == 'K') {
                behaviour.canStandOnTop = true;
            }

            if (c == 'R') {
                behaviour.isRoller = true;
            }

            if (c == 'P') {
                behaviour.isPublicSpaceObject = true;
            }

            if (c == 'I') {
                behaviour.isInvisible = true;
            }

            if (c == 'G') {
                behaviour.requiresRightsForInteraction = true;
            }

            if (c == 'T') {
                behaviour.requiresTouchingForInteraction = true;
            }

            if (c == 'U') {
                behaviour.customDataTrueFalse = true;
            }

            if (c == 'O') {
                behaviour.customDataOnOff = true;
            }

            if (c == 'M') {
                behaviour.customDataNumericOnOff = true;
            }

            if (c == 'Z') {
                behaviour.customDataNumericState = true;
            }

            if (c == 'H') {
                behaviour.canStackOnTop = true;
            }

            if (c == 'V') {
                behaviour.isDecoration = true;
            }

            if (c == 'J') {
                behaviour.isPostIt = true;
            }

            if (c == 'D') {
                behaviour.isDoor = true;
            }

            if (c == 'X') {
                behaviour.isTeleporter = true;
            }

            if (c == 'F') {
                behaviour.isDice = true;
            }

            if (c == 'Y') {
                behaviour.isPrizeTrophy = true;
            }

            if (c == 'Q') {
                behaviour.isRedeemable = true;
            }

            if (c == 'A') {
                behaviour.isSoundMachine = true;
            }

            if (c == 'N') {
                behaviour.isSoundMachineSampleSet = true;
            }
        }

        return behaviour;
    }

    public boolean isWallItem() {
        return isWallItem;
    }

    public void setWallItem(boolean wallItem) {
        isWallItem = wallItem;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    public boolean isCanSitOnTop() {
        return canSitOnTop;
    }

    public void setCanSitOnTop(boolean canSitOnTop) {
        this.canSitOnTop = canSitOnTop;
    }

    public boolean isCanLayOnTop() {
        return canLayOnTop;
    }

    public void setCanLayOnTop(boolean canLayOnTop) {
        this.canLayOnTop = canLayOnTop;
    }

    public boolean isCanStandOnTop() {
        return canStandOnTop;
    }

    public void setCanStandOnTop(boolean canStandOnTop) {
        this.canStandOnTop = canStandOnTop;
    }

    public boolean isCanStackOnTop() {
        return canStackOnTop;
    }

    public void setCanStackOnTop(boolean canStackOnTop) {
        this.canStackOnTop = canStackOnTop;
    }

    public boolean isRoller() {
        return isRoller;
    }

    public void setRoller(boolean roller) {
        isRoller = roller;
    }

    public boolean isPublicSpaceObject() {
        return isPublicSpaceObject;
    }

    public void setPublicSpaceObject(boolean publicSpaceObject) {
        isPublicSpaceObject = publicSpaceObject;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    public boolean isRequiresRightsForInteraction() {
        return requiresRightsForInteraction;
    }

    public void setRequiresRightsForInteraction(boolean requiresRightsForInteraction) {
        this.requiresRightsForInteraction = requiresRightsForInteraction;
    }

    public boolean isRequiresTouchingForInteraction() {
        return requiresTouchingForInteraction;
    }

    public void setRequiresTouchingForInteraction(boolean requiresTouchingForInteraction) {
        this.requiresTouchingForInteraction = requiresTouchingForInteraction;
    }

    public boolean isCustomDataTrueFalse() {
        return customDataTrueFalse;
    }

    public void setCustomDataTrueFalse(boolean customDataTrueFalse) {
        this.customDataTrueFalse = customDataTrueFalse;
    }

    public boolean isCustomDataOnOff() {
        return customDataOnOff;
    }

    public void setCustomDataOnOff(boolean customDataOnOff) {
        this.customDataOnOff = customDataOnOff;
    }

    public boolean isCustomDataNumericOnOff() {
        return customDataNumericOnOff;
    }

    public void setCustomDataNumericOnOff(boolean customDataNumericOnOff) {
        this.customDataNumericOnOff = customDataNumericOnOff;
    }

    public boolean isCustomDataNumericState() {
        return customDataNumericState;
    }

    public void setCustomDataNumericState(boolean customDataNumericState) {
        this.customDataNumericState = customDataNumericState;
    }

    public boolean isDecoration() {
        return isDecoration;
    }

    public void setDecoration(boolean decoration) {
        isDecoration = decoration;
    }

    public boolean isPostIt() {
        return isPostIt;
    }

    public void setPostIt(boolean postIt) {
        isPostIt = postIt;
    }

    public boolean isDoor() {
        return isDoor;
    }

    public void setDoor(boolean door) {
        isDoor = door;
    }

    public boolean isTeleporter() {
        return isTeleporter;
    }

    public void setTeleporter(boolean teleporter) {
        isTeleporter = teleporter;
    }

    public boolean isDice() {
        return isDice;
    }

    public void setDice(boolean dice) {
        isDice = dice;
    }

    public boolean isPrizeTrophy() {
        return isPrizeTrophy;
    }

    public void setPrizeTrophy(boolean prizeTrophy) {
        isPrizeTrophy = prizeTrophy;
    }

    public boolean isRedeemable() {
        return isRedeemable;
    }

    public void setRedeemable(boolean redeemable) {
        isRedeemable = redeemable;
    }

    public boolean isSoundMachine() {
        return isSoundMachine;
    }

    public void setSoundMachine(boolean soundMachine) {
        isSoundMachine = soundMachine;
    }

    public boolean isSoundMachineSampleSet() {
        return isSoundMachineSampleSet;
    }

    public void setSoundMachineSampleSet(boolean soundMachineSampleSet) {
        isSoundMachineSampleSet = soundMachineSampleSet;
    }

    public boolean isHas_extra_parameter() {
        return has_extra_parameter;
    }

    public void setHas_extra_parameter(boolean has_extra_parameter) {
        this.has_extra_parameter = has_extra_parameter;
    }
}
