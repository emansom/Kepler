package org.alexdev.kepler.messages;

import org.alexdev.kepler.game.player.Player;
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
    private ConcurrentHashMap<Short, List<MessageEvent>> messages;

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
        /*registerEvent(Incoming.VersionCheckMessageEvent, new VersionCheckMessageEvent());
        registerEvent(Incoming.InitCryptoMessageEvent, new InitCryptoMessageEvent());
        registerEvent(Incoming.GenerateSecretKeyMessageEvent, new GenerateSecretKeyMessageEvent());
        registerEvent(Incoming.UniqueIDMessageEvent, new UniqueIDMessageEvent());
        registerEvent(Incoming.AuthenticateMessageEvent, new AuthenticateMessageEvent());*/
    }

    /**
     * Unregister handshake packets.
     */
    public void unregisterHandshakePackets() {
        /*unregisterEvent(Incoming.VersionCheckMessageEvent);
        unregisterEvent(Incoming.InitCryptoMessageEvent);
        unregisterEvent(Incoming.GenerateSecretKeyMessageEvent);
        unregisterEvent(Incoming.UniqueIDMessageEvent);
        unregisterEvent(Incoming.AuthenticateMessageEvent);*/
    }

    /**
     * Register event.
     *
     * @param header the header
     * @param messageEvent the message event
     */
    private void registerEvent(Short header, MessageEvent messageEvent) {
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
    private void unregisterEvent(Short header) {
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
        if (Configuration.getInstance().getServerConfig().get("Logging", "log.received.packets", Boolean.class)) {
            if (this.messages.containsKey(message.getMessageId())) {
                MessageEvent event = this.messages.get(message.getMessageId()).get(0);
                this.player.getLogger().info("Received ({}): {} / {} ", event.getClass().getSimpleName(), message.getMessageId(), message.getMessageBody());
            } else {
                this.player.getLogger().info("Received ({}): {} / {} ", "Unknown", message.getMessageId(), message.getMessageBody());
            }
        }

        invoke(message.getMessageId(), message);
    }

    /**
     * Invoke the request.
     *
     * @param messageId the message id
     * @param message the message
     */
    private void invoke(short messageId, NettyRequest message) {
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
    public ConcurrentHashMap<Short, List<MessageEvent>> getMessages() {
        return messages;
    }
}