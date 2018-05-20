package org.alexdev.kepler.game.player;

import io.netty.util.AttributeKey;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.moderation.FuserightsManager;
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

    public Player(NettyPlayerNetwork nettyPlayerNetwork) {
        this.network = nettyPlayerNetwork;
        this.details = new PlayerDetails();
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
    }

    /**
     * Login handler for player
     */
    public void login() {
        PlayerManager.getInstance().addPlayer(this);
        this.log = LoggerFactory.getLogger("Player " + this.details.getName());

        this.send(new LOGIN());
        this.send(new FUSERIGHTS(FuserightsManager.getInstance().getAvailableFuserights(this.details.getRank())));

        log.info("Users: " + PlayerManager.getInstance().getPlayers().size());
    }

    /**
     * Check if the player has a permission for a rank.
     *
     * @param permission the permission
     * @return true, if successful
     */
    @Override
    public boolean hasPermission(String permission) {
        return false;
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

    @Override
    public PlayerDetails getDetails() {
        return this.details;
    }

    @Override
    public RoomUser getRoomUser() {
        return null;
    }

    @Override
    public EntityType getType() {
        return null;
    }

    public Logger getLogger() {
        return this.log;
    }

    public NettyPlayerNetwork getNetwork() {
        return this.network;
    }

    public MessageHandler getMessageHandler() {
        return new MessageHandler(this);
    }

    @Override
    public void dispose() {
        PlayerManager.getInstance().removePlayer(this);

        log.info("Users: " + PlayerManager.getInstance().getPlayers().size());
    }

    public void sendMessage(String entry) {

    }
}
