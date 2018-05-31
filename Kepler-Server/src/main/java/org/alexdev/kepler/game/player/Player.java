package org.alexdev.kepler.game.player;

import io.netty.util.AttributeKey;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.inventory.Inventory;
import org.alexdev.kepler.game.messenger.Messenger;
import org.alexdev.kepler.game.moderation.FuserightsManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.outgoing.handshake.FUSERIGHTS;
import org.alexdev.kepler.messages.outgoing.handshake.LOGIN;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Entity {
    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("Player");
    private final NettyPlayerNetwork network;

    private Logger log;
    private PlayerDetails details;
    private RoomUser roomUser;
    private Messenger messenger;
    private Inventory inventory;

    private boolean loggedIn;
    private boolean pingOK;

    public Player(NettyPlayerNetwork nettyPlayerNetwork) {
        this.network = nettyPlayerNetwork;
        this.details = new PlayerDetails();
        this.roomUser = new RoomUser(this);
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
        this.pingOK = true;
    }

    /**
     * Login handler for player
     */
    public void login() {
        this.log = LoggerFactory.getLogger("Player " + this.details.getName()); // Update logger to show name
        this.loggedIn = true;
        this.pingOK = true;

        PlayerManager.getInstance().disconnectSession(this.details.getId()); // Kill other sessions with same id
        PlayerManager.getInstance().addPlayer(this); // Add new connection

        this.messenger = new Messenger(this);
        this.inventory = new Inventory(this);

        this.sendQueued(new LOGIN());
        this.sendQueued(new FUSERIGHTS(FuserightsManager.getInstance().getAvailableFuserights(this.details.getRank())));

        if (GameConfiguration.getBoolean("welcome.message.enabled")) {
            String alertMessage = GameConfiguration.getString("welcome.message.contents");
            alertMessage = alertMessage.replace("%username%", this.details.getName());

            this.sendQueued(new ALERT(alertMessage));
        }

        this.flushSendQueue();

        PlayerDao.saveLastOnline(this.getDetails(), DateUtil.getCurrentTimeSeconds());
    }

    /**
     * Check if the player has a permission for a rank.
     *
     * @param fuse the permission
     * @return true, if successful
     */
    @Override
    public boolean hasFuse(String fuse) {
        return FuserightsManager.getInstance().hasFuseright(fuse, this.details.getRank());
    }

    /**
     * Send a response to the player
     *
     * @param response the response
     */
    public void send(MessageComposer response) {
        this.network.send(response);
    }

    /**
     * Defer a response to the player
     *
     * @param response the response
     */
    public void sendQueued(MessageComposer response) {
        this.network.enqueue(response);
    }

    /**
     * Flush send queue
     */
    public void flushSendQueue() {
        this.network.flush();
    }

    /**
     * Get the messenger instance for the player
     *
     * @return the messenger instance
     */
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public PlayerDetails getDetails() {
        return this.details;
    }

    @Override
    public RoomUser getRoomUser() {
        return this.roomUser;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    /**
     * Get the player logger.
     *
     * @return the logger
     */
    public Logger getLogger() {
        return this.log;
    }

    /**
     * Get the network handler for the player
     *
     * @return the network handler
     */
    public NettyPlayerNetwork getNetwork() {
        return this.network;
    }

    /**
     * Get if the player has logged in or not.
     *
     * @return true, if they have
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Get if the connection has timed out or not.
     *
     * @return false, if it hasn't.
     */
    public boolean isPingOK() {
        return pingOK;
    }

    /**
     * Set if the connection has timed out or not.
     *
     * @param pingOK the value to determine of the connection has timed out
     */
    public void setPingOK(boolean pingOK) {
        this.pingOK = pingOK;
    }

    /**
     * Get rid of the player from the server.
     */
    public void kickFromServer() {
        this.network.close();
        this.dispose();
    }

    /**
     * Dispose player when disconnect happens.
     */
    @Override
    public void dispose() {
        if (!this.loggedIn) {
            return;
        }

        PlayerDao.saveLastOnline(this.getDetails(), DateUtil.getCurrentTimeSeconds());
        PlayerManager.getInstance().removePlayer(this);

        if (this.roomUser.getRoom() != null) {
            this.roomUser.getRoom().getEntityManager().leaveRoom(this, false);
        }

        for (Room room : RoomManager.getInstance().replaceQueryRooms(RoomDao.getRoomsByUserId(this.details.getId()))) {
            room.tryDispose();
        }

        this.loggedIn = false;
    }

    public void sendMessage(String entry) {

    }
}
