package org.alexdev.kepler.messages;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.incoming.messenger.MESSENGERINIT;
import org.alexdev.kepler.messages.incoming.navigator.GETUSERFLATCATS;
import org.alexdev.kepler.messages.incoming.navigator.RECOMMENDED_ROOMS;
import org.alexdev.kepler.messages.incoming.navigator.SUSERF;
import org.alexdev.kepler.messages.incoming.rooms.*;
import org.alexdev.kepler.messages.incoming.handshake.GENERATEKEY;
import org.alexdev.kepler.messages.incoming.handshake.INIT_CRYPTO;
import org.alexdev.kepler.messages.incoming.handshake.SSO;
import org.alexdev.kepler.messages.incoming.navigator.NAVIGATE;
import org.alexdev.kepler.messages.incoming.rooms.settings.GETFLATINFO;
import org.alexdev.kepler.messages.incoming.rooms.user.QUIT;
import org.alexdev.kepler.messages.incoming.rooms.user.WALK;
import org.alexdev.kepler.messages.incoming.user.GET_CREDITS;
import org.alexdev.kepler.messages.incoming.user.GET_INFO;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler {

    private Player player;
    private ConcurrentHashMap<Integer, List<MessageEvent>> messages;

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(Player player) {
        this.player = player;
        this.messages = new ConcurrentHashMap<>();

        registerHandshakePackets();
        //if (Configuration.getInstance().getServerConfig().getInteractor("Logging", "log.items.loaded", Boolean.class)) {
        //    log.info("Loaded {} message event handlers", messages.size());
        //}
    }

    /**
     * Register handshake packets.
     */
    private void registerHandshakePackets() {
        registerEvent(206, new INIT_CRYPTO());
        registerEvent(202, new GENERATEKEY());
        registerEvent(204, new SSO());
    }

    /**
     * Unregister handshake packets.
     */
    public void unregisterHandshakePackets() {
        unregisterEvent(206);
        unregisterEvent(202);
        unregisterEvent(204);
    }

    /**
     * Register general purpose user packets.
     */
    public void registerUserPackets() {
        registerEvent(7, new GET_INFO());
        registerEvent(8, new GET_CREDITS());
    }

    /**
     * Register navigator packets.
     */
    public void registerNavigatorPackets() {
        registerEvent(150, new NAVIGATE());
        registerEvent(16, new SUSERF());
        registerEvent(151, new GETUSERFLATCATS());
        registerEvent(264, new RECOMMENDED_ROOMS());
    }

    /**
     * Register room packets.
     */
    public void registerRoomPackets() {
        registerEvent(57, new TRYFLAT());
        registerEvent(59, new GOTOFLAT());
        registerEvent(182, new GETINTEREST());
        registerEvent(2, new ROOM_DIRECTORY());
        registerEvent(126, new GETROOMAD());
        registerEvent(60, new G_HMAP());
        registerEvent(62, new G_OBJS());
        registerEvent(61,  new G_USRS());
        registerEvent(64, new G_STAT());
        registerEvent(63, new G_ITEMS());
    }

    /**
     * Register room user packets.
     */
    public void registerRoomUserPackets() {
        registerEvent(53, new QUIT());
        registerEvent(75, new WALK());
    }

    /**
     * Register room settings packets.
     */
    public void registerRoomSettingsPackets() {
        registerEvent(21, new GETFLATINFO());
    }

    /**
     * Register messenger packets.
     */
    public void registerMessengerPackets() {
        registerEvent(12, new MESSENGERINIT());
    }

    /**
     * Register event.
     *
     * @param header the header
     * @param messageEvent the message event
     */
    private void registerEvent(int header, MessageEvent messageEvent) {
        if (!this.messages.containsKey(header)) {
            this.messages.put(header, new ArrayList<>());
        }

        this.messages.get(header).add(messageEvent);
    }

    /**
     * Unegister event.
     *
     * @param header the header
     */
    private void unregisterEvent(int header) {
        List<MessageEvent> events = this.messages.get(header);

        if (events != null) {
            this.messages.remove(header);
        }
    }

    /**
     * Handle request.
     *
     * @param message the message
     */
    public void handleRequest(NettyRequest message) {
        try {
            if (Configuration.getInstance().getServerConfig().get("Logging", "log.received.packets", Boolean.class)) {
                if (this.messages.containsKey(message.getHeaderId())) {
                    MessageEvent event = this.messages.get(message.getHeaderId()).get(0);
                    this.player.getLogger().info("Received ({}): {} / {} ", event.getClass().getSimpleName(), message.getHeaderId(), message.getMessageBody());
                } else {
                    this.player.getLogger().info("Received ({}): {} / {} ", "Unknown", message.getHeaderId(), message.getMessageBody());
                }
            }
        } catch (Exception e) {
            Log.getErrorLogger().error("Exception occurred when handling (" + message.getHeaderId() + "): ", e);
        }

        invoke(message.getHeaderId(), message);
    }

    /**
     * Invoke the request.
     *
     * @param messageId the message id
     * @param message the message
     */
    private void invoke(int messageId, NettyRequest message) {
        if (this.messages.containsKey(messageId)) {
            for (MessageEvent event : this.messages.get(messageId)) {
                event.handle(this.player, message);
            }
        }

        message.dispose();
    }

    /**
     * Gets the messages.
     *
     * @return the messages
     */
    public ConcurrentHashMap<Integer, List<MessageEvent>> getMessages() {
        return messages;
    }
}