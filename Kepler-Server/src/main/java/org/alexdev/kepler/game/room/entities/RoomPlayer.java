package org.alexdev.kepler.game.room.entities;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.managers.RoomTimerManager;
import org.alexdev.kepler.game.room.managers.RoomTradeManager;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysEntrance;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysManager;
import org.alexdev.kepler.messages.outgoing.rooms.user.FIGURE_CHANGE;
import org.alexdev.kepler.messages.outgoing.user.USER_OBJECT;

import java.util.ArrayList;
import java.util.List;

public class RoomPlayer extends RoomEntity {
    private Player player;
    private RoomTimerManager timerManager;

    private int authenticateId;
    private int authenticateTelporterId;

    private boolean isTyping;
    private boolean isDiving;

    private Player tradePartner;
    private List<Item> tradeItems;
    private boolean tradeAccept;

    private String currentGameId;

    public RoomPlayer(Player player) {
        super(player);
        this.player = player;
        this.authenticateTelporterId = -1;

        this.tradeItems = new ArrayList<>();
        this.timerManager = new RoomTimerManager(this);
    }

    @Override
    public void reset() {
        super.reset();
        this.isTyping = false;
        this.isDiving = false;

        this.authenticateId = -1;
        this.timerManager.resetTimers();
        RoomTradeManager.close(this);
    }

    @Override
    public void stopWalking() {
        super.stopWalking();
    }

    /**
     * Refreshes user appearance
     */
    public void refreshAppearance() {
        var newDetails = PlayerDao.getDetails(this.player.getDetails().getId());

        // Reload figure, gender and motto
        this.player.getDetails().setFigure(newDetails.getFigure());
        this.player.getDetails().setSex(newDetails.getSex());
        this.player.getDetails().setMotto(newDetails.getMotto());

        // Send refresh to user
        this.player.send(new USER_OBJECT(this.player.getDetails()));

        // Send refresh to room if inside room
        if (this.getRoom() != null) {
            this.getRoom().send(new FIGURE_CHANGE(this.getInstanceId(), this.player.getDetails()));
        }
    }

    public RoomTimerManager getTimerManager() {
        return timerManager;
    }

    public int getAuthenticateId() {
        return authenticateId;
    }

    public void setAuthenticateId(int authenticateId) {
        this.authenticateId = authenticateId;
    }

    public int getAuthenticateTelporterId() {
        return authenticateTelporterId;
    }

    public void setAuthenticateTelporterId(int authenticateTelporterId) {
        this.authenticateTelporterId = authenticateTelporterId;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public boolean isDiving() {
        return isDiving;
    }

    public void setDiving(boolean diving) {
        isDiving = diving;
    }

    public Player getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(Player tradePartner) {
        this.tradePartner = tradePartner;
    }

    public List<Item> getTradeItems() {
        return tradeItems;
    }

    public boolean hasAcceptedTrade() {
        return tradeAccept;
    }

    public void setTradeAccept(boolean tradeAccept) {
        this.tradeAccept = tradeAccept;
    }

    public String getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(String currentGameId) {
        this.currentGameId = currentGameId;
    }
}