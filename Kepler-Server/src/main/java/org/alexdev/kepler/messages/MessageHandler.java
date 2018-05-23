package org.alexdev.kepler.messages;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.incoming.inventory.GETSTRIP;
import org.alexdev.kepler.messages.incoming.catalogue.GCAP;
import org.alexdev.kepler.messages.incoming.catalogue.GCIX;
import org.alexdev.kepler.messages.incoming.catalogue.GET_ALIAS_LIST;
import org.alexdev.kepler.messages.incoming.catalogue.GRPC;
import org.alexdev.kepler.messages.incoming.messenger.*;
import org.alexdev.kepler.messages.incoming.navigator.*;
import org.alexdev.kepler.messages.incoming.rooms.*;
import org.alexdev.kepler.messages.incoming.handshake.*;
import org.alexdev.kepler.messages.incoming.rooms.pool.SWIMSUIT;
import org.alexdev.kepler.messages.incoming.rooms.settings.*;
import org.alexdev.kepler.messages.incoming.rooms.user.*;
import org.alexdev.kepler.messages.incoming.user.*;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler {
    private ConcurrentHashMap<Integer, List<MessageEvent>> messages;

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private static MessageHandler instance;

    public MessageHandler() {
        this.messages = new ConcurrentHashMap<>();

        registerHandshakePackets();
        registerUserPackets();
        registerNavigatorPackets();
        registerRoomPackets();
        registerRoomUserPackets();
        registerRoomPoolPackets();
        registerRoomSettingsPackets();
        registerMessengerPackets();
        registerCataloguePackets();
        registerInventoryPackets();
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
     * Register general purpose user packets.
     */
    public void registerUserPackets() {
        registerEvent(7, new GET_INFO());
        registerEvent(8, new GET_CREDITS());
        registerEvent(196, new PONG());
        registerEvent(315, new TEST_LATENCY());
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
        registerEvent(115, new GOAWAY());
    }

    /**
     * Register room settings packets.
     */
    public void registerRoomSettingsPackets() {
        registerEvent(21, new GETFLATINFO());
        registerEvent(29, new CREATEFLAT());
        registerEvent(25, new SETFLATINFO());
        registerEvent(24, new UPDATEFLAT());
        registerEvent(153, new SETFLATCAT());
        registerEvent(152, new GETFLATCAT());
        registerEvent(23, new DELETEFLAT());
    }

    /**
     * Register room pool packets.
     */
    private void registerRoomPoolPackets() {
        registerEvent(116, new SWIMSUIT());
    }

    /**
     * Register messenger packets.
     */
    public void registerMessengerPackets() {
        registerEvent(12, new MESSENGERINIT());
        registerEvent(41, new FINDUSER());
        registerEvent(39, new MESSENGER_REQUESTBUDDY());
        registerEvent(38, new MESSENGER_DECLINEBUDDY());
        registerEvent(37, new MESSENGER_ACCEPTBUDDY());
        registerEvent(233, new MESSENGER_GETREQUESTS());
        registerEvent(191, new MESSENGER_GETMESSAGES());
        registerEvent(36, new MESSENGER_ASSIGNPERSMSG());
        registerEvent(40, new MESSENGER_REMOVEBUDDY());
        registerEvent(33, new MESSENGER_SENDMSG());
        registerEvent(32, new MESSENGER_MARKREAD());
        registerEvent(262, new FOLLOW_FRIEND());
        registerEvent(15, new FRIENDLIST_UPDATE());
    }

    /**
     * Register catalogue packets
     */
    private void registerCataloguePackets() {
        registerEvent(101, new GCIX());
        registerEvent(102, new GCAP());
        registerEvent(100, new GRPC());
        registerEvent(215, new GET_ALIAS_LIST());
    }

    /**
     * Register inventory packets.
     */
    private void registerInventoryPackets() {
        registerEvent(65, new GETSTRIP());
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
    public void handleRequest(Player player, NettyRequest message) {
        try {
            if (Configuration.getBoolean("log.received.packets")) {
                if (this.messages.containsKey(message.getHeaderId())) {
                    MessageEvent event = this.messages.get(message.getHeaderId()).get(0);
                    player.getLogger().info("Received ({}): {} / {} ", event.getClass().getSimpleName(), message.getHeaderId(), message.getMessageBody());
                } else {
                    player.getLogger().info("Received ({}): {} / {} ", "Unknown", message.getHeaderId(), message.getMessageBody());
                }
            }
        } catch (Exception e) {
            Log.getErrorLogger().error("Exception occurred when handling (" + message.getHeaderId() + "): ", e);
        }

        invoke(player, message.getHeaderId(), message);
    }

    /**
     * Invoke the request.
     *
     * @param messageId the message id
     * @param message the message
     */
    private void invoke(Player player, int messageId, NettyRequest message) {
        if (this.messages.containsKey(messageId)) {
            for (MessageEvent event : this.messages.get(messageId)) {
                event.handle(player, message);
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

    /**
     * Get the instance of {@link RoomManager}
     *
     * @return the instance
     */
    public static MessageHandler getInstance() {
        if (instance == null) {
            instance = new MessageHandler();
        }

        return instance;
    }
}