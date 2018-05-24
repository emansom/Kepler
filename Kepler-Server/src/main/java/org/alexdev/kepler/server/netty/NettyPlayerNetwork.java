package org.alexdev.kepler.server.netty;

import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.alexdev.kepler.messages.types.MessageComposer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NettyPlayerNetwork {
    private Channel channel;
    private int connectionId;
    private final Queue<MessageComposer> messageQueue = new ConcurrentLinkedQueue<>();

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

    public void enqueue(MessageComposer response) {
        messageQueue.add(response);
    }

    public void flush() {
        while (!messageQueue.isEmpty()) {
            channel.write(messageQueue.remove());
        }

        channel.flush();
    }

    public int getConnectionId() {
        return connectionId;
    }
}
