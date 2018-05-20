package org.alexdev.kepler.server.netty.streams;

import io.netty.buffer.ByteBuf;
import org.alexdev.kepler.util.encoding.Base64Encoding;
import org.alexdev.kepler.util.encoding.VL64Encoding;

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

    public void write(Object obj) {
        this.buffer.writeBytes(obj.toString().getBytes());
    }

    public void writeString(Object obj) {
        this.buffer.writeBytes(obj.toString().getBytes());
        this.buffer.writeByte(2);
    }

    public void writeInt(Integer obj) {
        this.buffer.writeBytes(VL64Encoding.encodeVL64(obj));
    }

    public void writeShort(int obj) {
        this.buffer.writeShort((short)obj);
    }

    public void writeBool(Boolean obj) {
        this.writeInt(obj ? 1 : 0);
    }

    public String getBodyString() {
        String str = this.buffer.toString(Charset.defaultCharset());
        
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
