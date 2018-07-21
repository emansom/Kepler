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

    public Channel getChannel() {
        return this.channel;
    }

    public void send(Object response) {
        this.channel.writeAndFlush(response).syncUninterruptibly();
    }

    public void sendQueued(MessageComposer response) {
        this.channel.write(response);
    }

    public void flush() {
        this.channel.flush();
    }

    public void close() {
        this.channel.close();
    }

    public int getConnectionId() {
        return connectionId;
    }
}
