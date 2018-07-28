package org.alexdev.kepler.game.moderation;

import org.alexdev.kepler.game.player.Player;

import java.util.HashMap;

public class CallForHelp {

    private final int callId;
    private final String caller;
    private final String message;
    private String pickedUpBy;
    private final String roomName;
    private final int callerId;
    private final int roomId;
    private int priority = 2;

    CallForHelp(int callId, Player caller, String message){
        this.callId = callId;
        this.caller = caller.getDetails().getName();
        this.callerId = caller.getDetails().getId();
        this.message = message;
        this.pickedUpBy = "";
        this.roomId = caller.getRoomUser().getRoom().getData().getId();
        this.roomName = caller.getRoomUser().getRoom().getData().getName();

    }

    public String getMessage(){
        return this.message;
    }

    public String getRoomName(){
        return this.roomName;
    }

    public String getPickedUpBy(){
        return this.pickedUpBy;
    }

    public int getRoomId(){
        return this.roomId;
    }

    public int getPriority(){
        return this.priority;
    }

    public String getCaller(){
        return this.caller;
    }

    public int getCallId(){
        return this.callId;
    }

    public boolean isOpen(){
        return this.pickedUpBy.equals("");
    }

    void pickUp(Player moderator){
        if(moderator.hasFuse("fuse_cfh")) {
            this.pickedUpBy = moderator.getDetails().getName();
        }
    }

}
