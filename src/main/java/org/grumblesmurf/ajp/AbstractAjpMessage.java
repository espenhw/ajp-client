package org.grumblesmurf.ajp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

abstract class AbstractAjpMessage
    implements AjpMessage
{
    private static final byte[] AJP_TAG = { 0x12, 0x34 };
    
    private final ByteArrayOutputStream bos;

    AbstractAjpMessage(int packetType) throws IOException {
        bos = new ByteArrayOutputStream();
        bos.write(AJP_TAG);
        // Write two placeholder bytes for the length
        bos.write(0);
        bos.write(0);
        bos.write(packetType);
    }

    public final void writeTo(OutputStream out) throws IOException {
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
}
