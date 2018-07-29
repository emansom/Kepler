package org.alexdev.kepler.game.moderation;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.moderation.CALL_FOR_HELP;
import org.alexdev.kepler.messages.outgoing.moderation.PICKED_CRY;
import org.alexdev.kepler.messages.outgoing.user.CRY_RECEIVED;
import org.alexdev.kepler.messages.types.MessageComposer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CallForHelpManager {

    private static CallForHelpManager instance;
    private List<CallForHelp> callsForHelp;
    private int latestCallId = 0;

    public CallForHelpManager(){
        this.callsForHelp = new CopyOnWriteArrayList<>();
    }

    /**
     * Add Call for Help to queue
     *
     * @param caller The person submitting the CFH
     * @param message The message attached to the CFH
     */
    public void submitCallForHelp(Player caller, String message){
        if(this.getOpenCallForHelpByPlayerName(caller.getDetails().getName()) == null){
            CallForHelp cfh = new CallForHelp(latestCallId++, caller, message);
            this.callsForHelp.add(cfh);
            sendToModerators(new CALL_FOR_HELP(cfh));
            caller.send(new CRY_RECEIVED());
        }
    }

    /**
     * Get open Call for Help by Player Username
     *
     * @param id the ID to fetch
     * @return the Call for Help, null if  no open Calls for Help
     */
    public CallForHelp getCallForHelpById(int id){
        for(CallForHelp cfh : this.callsForHelp){
            if(cfh.getCallId() == id){
                return cfh;
            }
        }
        return null;
    }

    /**
     * Get Call for Help by Player Username
     *
     * @param username the username to get with
     * @return the open Call for Help, null if  no open Calls for Help
     */
    public CallForHelp getOpenCallForHelpByPlayerName(String username){
        for(CallForHelp cfh : this.callsForHelp){
            if(cfh.isOpen() && cfh.getCaller().equals(username)){
                return cfh;
            }
        }
        return null;
    }

    /**
     * Send a packet to all online staff
     * @param message the Message to send
     */
    void sendToModerators(MessageComposer message){
        for(Player p : PlayerManager.getInstance().getActivePlayers()){
            if(p.hasFuse("fuse_cfh")){
                p.send(message);
            }
        }
    }

    /**
     * Pick up a call for help
     * @param callId the ID of the CFH to pick up
     * @param moderator the moderator who is picking it up
     */
    public void pickUp(int callId, Player moderator){
        if(moderator.hasFuse("fuse_cfh")){
            CallForHelp cfh = this.getCallForHelpById(callId);
            if(cfh != null) {
                cfh.pickUp(moderator);
                // Send the updated CallForHelp to Moderators
                sendToModerators(new PICKED_CRY(cfh));
            }
        }
    }

    /**
     * Chnage catgeory of Call
     * @param callId the ID of the call to change
     * @param newCategory the new category
     */
    public void changeCategory(int callId, int newCategory){
        CallForHelp cfh = this.getCallForHelpById(callId);
        if(cfh != null) {
            cfh.updateCategory(newCategory);
            // Send the updated CallForHelp to Moderators
            sendToModerators(new CALL_FOR_HELP(cfh));
        }
    }

    /**
     * Gets the instance
     *
     * @return the instance
     */
    public static CallForHelpManager getInstance(){
        if (instance == null) {
            instance = new CallForHelpManager();
        }
        return instance;
    }

}
