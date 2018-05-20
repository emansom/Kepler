package org.alexdev.icarus.server.netty.streams;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class NettyRequest {

    final private short header;
    final private int length;

    final public ByteBuf buffer;

    public NettyRequest(int length, ByteBuf buffer) {
        this.buffer = buffer;
        this.header = buffer.readShort();
        this.length = length;
    }

    public Integer readInt() {
        try {
            return this.buffer.readInt();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean readIntAsBool() {
        try {
            return this.buffer.readInt() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean readBoolean()  {
        
        try {
            return this.buffer.readByte() == 1;
        } catch (Exception e)    {
            return false;
        }
    }

    public String readString() {
        try {
            int length = this.buffer.readShort();
            byte[] data = this.readBytes(length);

            return new String(data);
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] readBytes(int len) {
        try {
            byte[] payload = new byte[len];
            this.buffer.readBytes(payload);
            return payload;

        } catch (Exception e) {
            return null;
        }
    }

    public byte[] remainingBytes() {
        try {
            this.buffer.markReaderIndex();

            byte[] bytes = new byte[this.buffer.readableBytes()];
            buffer.readBytes(bytes);

            this.buffer.resetReaderIndex();
            return bytes;

        } catch (Exception e) {
            return null;
        }
    }

    public String getMessageBody() {
        String consoleText = this.buffer.toString(Charset.defaultCharset());

        for (int i = 0; i < 13; i++) {
            consoleText = consoleText.replace(Character.toString((char)i), "[" + i + "]");
        }

        return consoleText;
    }

    public short getMessageId() {
        return header;
    }

    public int getLength() {
        return length;
    }

    public void dispose() {
        this.buffer.release();
    }
}