package org.grumblesmurf.ajp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

abstract class AbstractAjpMessage
    implements AjpMessage
{
    private static final byte[] AJP_TAG = { 0x12, 0x34 };
    
    private final ByteArrayOutputStream bos;

    AbstractAjpMessage(int packetType) {
        bos = new ByteArrayOutputStream();
        bos.write(AJP_TAG, 0, AJP_TAG.length);
        // Write two placeholder bytes for the length
        bos.write(0);
        bos.write(0);
        bos.write(packetType);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(bytes());
    }
    
    public final byte[] bytes() {
        byte[] bytes = bos.toByteArray();
        int length = bytes.length - 4;
        if (length == -1) {
            bytes[2] = -1;
            bytes[3] = -1;
        } else {
            bytes[2] = (byte)((length & 0xff00) >> 8);
            bytes[3] = (byte)(length & 0x00ff);
        }
        return bytes;
    }

    protected void writeByte(int b) {
        bos.write(b);
    }

    protected void writeInt(int i) {
        bos.write((i & 0xff00) >> 8);
        bos.write(i & 0x00ff);
    }

    protected void writeBoolean(boolean b) {
        bos.write(b ? 1 : 0);
    }

    protected void writeString(String s) {
        if (s == null) {
            bos.write(-1);
        } else {
            writeInt(s.length());
            try {
                // XXX: Is this right?
                byte[] buf = s.getBytes("UTF-8");
                bos.write(buf, 0, buf.length);
                bos.write('\0');
            } catch (UnsupportedEncodingException e) {
                // WTF?
                throw new RuntimeException("Your JVM is broken", e);
            }
        }
    }
}
