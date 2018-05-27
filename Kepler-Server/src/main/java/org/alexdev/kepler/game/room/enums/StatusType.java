package org.alexdev.kepler.game.room.enums;

public enum StatusType {
    MOVE("mv"),
    SIT("sit"),
    LAY("lay"), 
    FLAT_CONTROL("flatctrl"), 
    DANCE("dance"),
    SWIM("swim"),
    BLANK(""),
    CARRY_ITEM("cri"),
    USE_ITEM("usei"),
    CARRY_FOOD("carryf"),
    USE_FOOD("eat"),
    CARRY_DRINK("carryd"),
    USE_DRINK("drink");

    private String statusCode;

    StatusType(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
