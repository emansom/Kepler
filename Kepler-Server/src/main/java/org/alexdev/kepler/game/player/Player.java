package org.alexdev.kepler.game.player;

import io.netty.util.AttributeKey;
import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.messenger.Messenger;
import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.moderation.FuserightsManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.MessageHandler;
import org.alexdev.kepler.messages.outgoing.handshake.FUSERIGHTS;
import org.alexdev.kepler.messages.outgoing.handshake.LOGIN;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Entity {
    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("Player");
    private final NettyPlayerNetwork network;

    private Logger log;
    private PlayerDetails details;
    private RoomUser roomUser;
    private Messenger messenger;
    private boolean loggedIn;

    public Player(NettyPlayerNetwork nettyPlayerNetwork) {
        this.network = nettyPlayerNetwork;
        this.details = new PlayerDetails();
        this.roomUser = new RoomUser(this);
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
    }

    /**
     * Login handler for player
     */
    public void login() {
        PlayerManager.getInstance().disconnectSession(this.details.getId()); // Kill other sessions with same id
        PlayerManager.getInstance().addPlayer(this); // Add new connection

        RoomManager.getInstance().addRoomsByUser(this.details.getId());

        this.loggedIn = true;
        this.messenger = new Messenger(this);

        // Update logger to show name
        this.log = LoggerFactory.getLogger("Player " + this.details.getName());

        this.send(new LOGIN());
        this.send(new FUSERIGHTS(FuserightsManager.getInstance().getAvailableFuserights(this.details.getRank())));
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
        try {
            this.network.send(response);
        } catch (Exception e) {
            Log.getErrorLogger().error("[Player] Could not send message to player {}", this.details.getName());
        }
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

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

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
     * Dispose player when disconnect happens.
     */
    @Override
    public void dispose() {
        if (!this.loggedIn) {
            return;
        }

        PlayerManager.getInstance().removePlayer(this);

        if (this.roomUser.getRoom() != null) {
            this.roomUser.getRoom().getEntityManager().leaveRoom(this, false);
        }

        for (Room room : RoomManager.getInstance().replaceQueryRooms(RoomDao.getRoomsByUserId(this.details.getId()))) {
            room.dispose();
        }
    }

    public void sendMessage(String entry) {

    }
}
