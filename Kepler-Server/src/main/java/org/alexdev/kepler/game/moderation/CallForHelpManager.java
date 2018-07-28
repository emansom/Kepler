package org.alexdev.kepler.game.moderation;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.moderation.CALL_FOR_HELP;

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
     * @return True if CFH was submitted successfully
     */
    public boolean submitCallForHelp(Player caller, String message){
        if(this.getOpenCallForHelpByPlayerName(caller.getDetails().getName()) == null){
            CallForHelp cfh = new CallForHelp(latestCallId++, caller, message);
            this.callsForHelp.add(cfh);
            sendToModerators(cfh);
            return true;
        }
        return false;
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
     * Send the Call for Help to all online staff
     * @param cfh the CFH to send
     */
    void sendToModerators(CallForHelp cfh){
        for(Player p : PlayerManager.getInstance().getActivePlayers()){
            if(p.hasFuse("fuse_cfh")){
                p.send(new CALL_FOR_HELP(cfh));
            }
        }
    }

    public void pickUp(int callId, Player moderator){
        if(moderator.hasFuse("fuse_cfh")){
            CallForHelp cfh = this.getCallForHelpById(callId);
            cfh.pickUp(moderator);
            // Send the updated CallForHelp to Moderators
            sendToModerators(cfh);
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
