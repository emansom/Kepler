package org.alexdev.kepler.server.netty;

import io.netty.channel.Channel;
import org.alexdev.kepler.messages.types.MessageComposer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NettyPlayerNetwork {
    private Channel channel;
    private int connectionId;

    public NettyPlayerNetwork(Channel channel, int connectionId) {
        this.channel = channel;
        this.connectionId = connectionId;
    }

    public void close() {
        channel.close();
    }

    public void send(MessageComposer response) {
        channel.writeAndFlush(response);
    }

    public int getConnectionId() {
        return connectionId;
    }
}
