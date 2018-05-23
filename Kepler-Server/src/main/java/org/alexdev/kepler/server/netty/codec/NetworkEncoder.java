package org.alexdev.kepler.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NetworkEncoder extends MessageToMessageEncoder<MessageComposer> {
    final private static Logger log = LoggerFactory.getLogger(NetworkEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageComposer msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();

        NettyResponse response = new NettyResponse(msg.getHeader(), buffer);
        msg.compose(response);

        if (!response.isFinalised()) {
            buffer.writeByte(1);
            response.setFinalised(true);
        }

        if (Configuration.getBoolean("log.sent.packets")) {
            log.info("SENT: {} / {}", msg.getHeader(), response.getBodyString());
        }

        out.add(buffer);
    }
}