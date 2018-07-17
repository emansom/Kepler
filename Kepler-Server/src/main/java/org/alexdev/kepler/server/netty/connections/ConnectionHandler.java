package org.alexdev.kepler.server.netty.connections;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.alexdev.kepler.Kepler;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.MessageHandler;
import org.alexdev.kepler.messages.outgoing.handshake.HELLO;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.alexdev.kepler.server.netty.NettyServer;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler extends SimpleChannelInboundHandler<NettyRequest> {
    final private static Logger log = LoggerFactory.getLogger(ConnectionHandler.class);
    final private NettyServer server;

    public ConnectionHandler(NettyServer server) {
        this.server = server;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Player player = new Player(new NettyPlayerNetwork(ctx.channel(), this.server.getConnectionIds().getAndIncrement()));
        ctx.channel().attr(Player.PLAYER_KEY).set(player);

        if (!server.getChannels().add(ctx.channel()) || Kepler.getIsShutdown()) {
            ctx.disconnect();
            return;
        }

        player.send(new HELLO());

        //if (ServerConfiguration.getBoolean("log.connections")) {
        log.info("[{}] Connection from {} ", player.getNetwork().getConnectionId(), ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0]);
        //}
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        this.server.getConnectionIds().getAndDecrement(); // Decrement because we don't want it to reach Integer.MAX_VALUE
        this.server.getChannels().remove(ctx.channel());

        Player player = ctx.channel().attr(Player.PLAYER_KEY).get();

        //if (ServerConfiguration.getBoolean("log.connections")) {
        log.info("[{}] Disonnection from {} ", player.getNetwork().getConnectionId(), ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0]);
        //}

        player.dispose();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, NettyRequest message) throws Exception {
        try {
            Player player = ctx.channel().attr(Player.PLAYER_KEY).get();

            if (message == null) {
                return;
            }

            if (player != null){
                MessageHandler.getInstance().handleRequest(player, message);
            }

        } catch (Exception ex) {
            Log.getErrorLogger().error("Exception occurred when handling (" + message.getHeaderId() + "): ", ex);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        try {
            Player player = ctx.channel().attr(Player.PLAYER_KEY).get();

            if (player.isDisconnected()) {
                return;
            }

            ctx.flush();
        } catch (Exception ex) {
            Log.getErrorLogger().error("Exception occurred handling channelReadComplete: ", ex);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.getErrorLogger().error("Netty error occurred: ", cause);
    }
}