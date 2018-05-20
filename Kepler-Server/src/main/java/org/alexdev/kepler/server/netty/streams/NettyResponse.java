package org.alexdev.kepler.server.netty.streams;

import io.netty.buffer.ByteBuf;
import org.alexdev.kepler.util.encoding.Base64Encoding;

import java.nio.charset.Charset;

public class NettyResponse  {
    private short id;
    private ByteBuf buffer;
    private boolean finalised;

    public NettyResponse(short header, ByteBuf buffer) {
        this.id = header;
        this.buffer = buffer;
        this.buffer.writeBytes(Base64Encoding.encodeB64(header, 2));
    }

    public void writeString(Object obj) {
        buffer.writeBytes(obj.toString().getBytes());
        buffer.writeByte(2);
    }

    public void writeInt(Integer obj) {
       buffer.writeInt(obj);
    }

    public void writeInt(Boolean obj) {
        buffer.writeInt(obj ? 1 : 0);
    }

    public void writeShort(int obj) {
        buffer.writeShort((short)obj);
    }

    public void writeBool(Boolean obj) {
        buffer.writeBoolean(obj);
    }

    public String getBodyString() {
        String str = new String(this.buffer.toString(Charset.defaultCharset()));
        
        for (int i = 0; i < 14; i++) { 
            str = str.replace(Character.toString((char)i), "[" + i + "]");
        }

        return str;
    }

    /**
     * If this packet has been finalised before sending
     *
     * @return true, if it was
     */
    public boolean isFinalised() {
        return finalised;

    }

    public void setFinalised(boolean finalised) {
        this.finalised = finalised;
    }

    /* (non-Javadoc)
     * @see org.alexdev.icarus.server.api.messages.Response#getHeader()
     */
    public int getHeader() {
        return this.id;
    }
}
