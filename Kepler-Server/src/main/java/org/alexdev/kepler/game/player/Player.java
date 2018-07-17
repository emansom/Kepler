package org.alexdev.kepler.game.player;

import io.netty.util.AttributeKey;
import org.alexdev.kepler.dao.mysql.PlayerDao;

import org.alexdev.kepler.game.club.ClubSubscription;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.inventory.Inventory;
import org.alexdev.kepler.game.messenger.Messenger;
import org.alexdev.kepler.game.moderation.FuserightsManager;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.outgoing.handshake.FUSERIGHTS;
import org.alexdev.kepler.messages.outgoing.handshake.LOGIN;
import org.alexdev.kepler.messages.outgoing.rooms.user.FIGURE_CHANGE;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.messages.outgoing.user.USER_OBJECT;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.alexdev.kepler.util.config.ServerConfiguration;
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
    private boolean disconnected;
    private boolean pingOK;

    public Player(NettyPlayerNetwork nettyPlayerNetwork) {
        this.network = nettyPlayerNetwork;
        this.details = new PlayerDetails();
        this.roomUser = new RoomUser(this);
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
        this.pingOK = true;
        this.disconnected = false;
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

        PlayerDao.saveLastOnline(this.getDetails(), DateUtil.getCurrentTimeSeconds());

        if (!ServerConfiguration.getBoolean("debug")) {
            PlayerDao.clearSSOTicket(this.details.getId()); // Protect against replay attacks
        }

        this.messenger = new Messenger(this);
        this.inventory = new Inventory(this);

        this.send(new LOGIN());
        this.refreshFuserights();

        if (GameConfiguration.getInstance().getBoolean("welcome.message.enabled")) {
            String alertMessage = GameConfiguration.getInstance().getString("welcome.message.content");
            alertMessage = alertMessage.replace("%username%", this.details.getName());

            this.send(new ALERT(alertMessage));
        }
    }

    /**
     * Refresh club for player.
     */
    public void refreshClub() {
        if (!this.details.hasClubSubscription()) {
            // If the database still thinks we have Habbo club even after it expired, reset it back to 0.
            if (this.details.getClubExpiration() > 0) {
                this.details.setFirstClubSubscription(0);
                this.details.setClubExpiration(0);
                this.details.getBadges().remove("HC1"); // If their HC ran out, remove badge.
                this.details.getBadges().remove("HC2"); // No gold badge when not subscribed.

                this.refreshFuserights();
                PlayerDao.saveSubscription(this.details);
            }
        } else {
            if (!this.details.getBadges().contains("HC1")) {
                this.details.getBadges().add("HC1");
            }

            if (this.details.hasGoldClubSubscription()) {
                if (!this.details.getBadges().contains("HC2")) {
                    this.details.getBadges().add("HC2");
                }
            }
        }

        ClubSubscription.refreshSubscription(this);
    }

    /**
     * Send fuseright permissions for player.
     */
    public void refreshFuserights() {
        this.send(new FUSERIGHTS(FuserightsManager.getInstance().getAvailableFuserights(
                this.details.hasClubSubscription(),
                this.details.getRank()))
        );
    }

    /**
     * Refreshes user appearance
     */
    public void refreshAppearance() {
        var newDetails = PlayerDao.getDetails(this.details.getId());

        // Reload figure, gender and motto
        this.details.setFigure(newDetails.getFigure());
        this.details.setSex(newDetails.getSex());
        this.details.setMotto(newDetails.getMotto());

        // Send refresh to user
        this.send(new USER_OBJECT(this.details));

        // Send refresh to room if inside room
        var room = this.roomUser.getRoom();

        if (room != null) {
            room.send(new FIGURE_CHANGE(this.roomUser.getInstanceId(), this.details));
        }
    }

    /**
     * Check if the player has a permission for a rank.
     *
     * @param fuse the permission
     * @return true, if successful
     */
    @Override
    public boolean hasFuse(String fuse) {
        return FuserightsManager.getInstance().hasFuseright(fuse,
                this.details.getRank(),
                this.details.hasClubSubscription());
    }

    /**
     * Send a response to the player
     *
     * @param response the response
     */
    public void send(MessageComposer response) {
        if (this.disconnected) {
            return;
        }

        this.network.send(response);
    }

    /**
     * Send a object to the player
     *
     * @param object the object to send
     */
    public void sendObject(Object object) {
        if (this.disconnected) {
            return;
        }

        this.network.send(object);
    }


    /**
     * Send a queued response to the player
     *
     * @param response the response
     */
    public void sendQueued(MessageComposer response) {
        if (this.disconnected) {
            return;
        }

        this.network.sendQueued(response);
    }

    /**
     * Flush queue
     */
    public void flush() {
        if (this.disconnected) {
            return;
        }

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
    public void kickFromServer(boolean closeSocket) {
        this.dispose();

        try {
            if (closeSocket) {
                this.network.close();
            }
        } catch (Exception ignored) { }

    }

    /**
     * Dispose player when disconnect happens.
     */
    @Override
    public void dispose() {
        if (this.loggedIn) {
            if (this.roomUser.getRoom() != null) {
                this.roomUser.getRoom().getEntityManager().leaveRoom(this, false);
            }

            PlayerDao.saveLastOnline(this.getDetails(), DateUtil.getCurrentTimeSeconds());
            PlayerManager.getInstance().removePlayer(this);
        }

        this.disconnected = true;
        this.loggedIn = false;
    }
}
