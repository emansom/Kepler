package org.alexdev.kepler.game.moderation;

import org.alexdev.kepler.game.player.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CallForHelp {

    private final int callId;
    private final String caller;
    private final String message;
    private String pickedUpBy;
    private final String roomName;
    private final int roomId;
    private final long requestTime;
    private int category = 2;

    CallForHelp(int callId, Player caller, String message){
        System.out.println(callId);
        this.callId = callId;
        this.caller = caller.getDetails().getName();
        this.message = message;
        this.pickedUpBy = "";
        this.roomId = caller.getRoomUser().getRoom().getData().getId();
        this.roomName = caller.getRoomUser().getRoom().getData().getName();
        this.requestTime = System.currentTimeMillis();
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

    public int getCategory(){
        return this.category;
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

    public String getFormattedRequestTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("H:m d/MM/YYYY");
        Date resultDate = new Date(this.requestTime);
        return sdf.format(resultDate);
    }

    void updateCategory(int newCategory){
        this.category = newCategory;
    }

    void pickUp(Player moderator){
        if(moderator.hasFuse("fuse_cfh")) {
            this.pickedUpBy = moderator.getDetails().getName();
        }
    }

}
