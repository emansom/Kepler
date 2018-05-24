package org.alexdev.kepler.server.netty.streams;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import org.alexdev.kepler.util.encoding.Base64Encoding;
import org.alexdev.kepler.util.encoding.VL64Encoding;

public class NettyRequest {
    final private int headerId;
    final private String header;
    final private int length;
    final private ByteBuf buffer;

    public NettyRequest(int length, ByteBuf buffer) {
        this.buffer = buffer;
        this.length = length;
        this.header = new String(new byte[] { buffer.readByte(), buffer.readByte() });
        this.headerId = Base64Encoding.decode(header.getBytes());
    }

    public Integer readInt() {
        try {
            AtomicInteger length = new AtomicInteger(0);
            int value = VL64Encoding.decode(remainingBytes(), length);

            readBytes(length.get());
            return value;
        } catch (Exception e) {
            return 0;
        }
    }

    public int readBase64() {
        return Base64Encoding.decode(new byte[] {
                this.buffer.readByte(),
                this.buffer.readByte()
        });
    }

    public boolean readBoolean()  {
        try {
            return this.readInt() == 1;
        } catch (Exception e)    {
            return false;
        }
    }

    public String readString() {
        try {
            int length = this.readBase64();
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

    public String contents() {
        try {
            return new String(this.remainingBytes());
        } catch (Exception e) {
            return null;
        }
    }

    public String getMessageBody() {
        String consoleText = this.buffer.toString(Charset.defaultCharset());

        for (int i = 0; i < 14; i++) {
            consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
        }

        return this.header + consoleText;
    }

    public String getHeader() {
        return header;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void dispose() {
        this.buffer.release();
    }
}