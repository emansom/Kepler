package org.alexdev.kepler.server.netty.streams;

import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;

public class NettyResponse  {

    private short id;
    private ByteBuf buffer;

    public NettyResponse(short header, ByteBuf buffer) {
        this.id = header;
        this.buffer = buffer;
        this.buffer.writeInt(-1);
        this.buffer.writeShort(id);
    }

    public void writeString(Object obj) {
        buffer.writeShort(obj.toString().length());
        buffer.writeBytes(obj.toString().getBytes());
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


    /* (non-Javadoc)
     * @see org.alexdev.icarus.server.api.messages.Response#getBodyString()
     */
    public String getBodyString() {
        String str = new String(this.buffer.toString(Charset.defaultCharset()));
        
        for (int i = 0; i < 14; i++) { 
            str = str.replace(Character.toString((char)i), "[" + i + "]");
        }

        return str;
    }

    /**
     * Gets has the length been set
     *
     * @return true, if the length was set
     */
    public boolean hasLength() {
        return (this.buffer.getInt(0) > -1);

    }

    /* (non-Javadoc)
     * @see org.alexdev.icarus.server.api.messages.Response#getHeader()
     */
    public int getHeader() {
        return this.id;
    }
}
