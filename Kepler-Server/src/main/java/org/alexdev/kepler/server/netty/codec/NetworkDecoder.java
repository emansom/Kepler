package org.alexdev.kepler.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.encoding.Base64Encoding;

import java.util.List;

public class NetworkDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.readableBytes() < 5) {
            // If the incoming data is less than 5 bytes, it's junk.
            return;
        }

        buffer.markReaderIndex();
        int length = Base64Encoding.decode(new byte[] { buffer.readByte(), buffer.readByte(), buffer.readByte() });

        if (buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            return;
        }

        if (length < 0) {
            return;
        }

        out.add(new NettyRequest(buffer.readBytes(length)));

        //int messageHeader = Base64Encoding.decodeB64(new byte[] { buffer.readByte(), buffer.readByte() });



        //System.out.println("Header: " + messageHeader) + " with length " + messageLength));

        /*byte delimiter = buffer.readByte();
        buffer.resetReaderIndex();

        if (delimiter == 60) {
            String policy = "<?xml version=\"1.0\"?>\r\n"
                    + "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n"
                    + "<cross-domain-policy>\r\n"
                    + "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n"
                    + "</cross-domain-policy>\0)";

            ChannelFuture future = ctx.channel().writeAndFlush(Unpooled.copiedBuffer(policy.getBytes()));
            future.addListener(ChannelFutureListener.CLOSE);

        } else {
            buffer.markReaderIndex();
            int length = buffer.readInt();

            if (buffer.readableBytes() < length) {
                buffer.resetReaderIndex();
                return;
            }

            if (length < 0) {
                return;
            }

            out.add(new NettyRequest(length, buffer.readBytes(length)));
        }*/
    }
}