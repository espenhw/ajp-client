package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

class SendBodyChunkMessage
    extends AbstractAjpReplyMessage
{
    final int length;
    final byte[] bytes;

    SendBodyChunkMessage(int length, byte[] bytes) {
        this.length = length;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        try {
            return new String(bytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Your JVM is broken", e);
        }
    }

    static SendBodyChunkMessage readFrom(InputStream in) throws IOException {
        int length = AjpReader.readInt(in);
        byte[] bytes = new byte[length];
        AjpReader.fullyRead(bytes, in);
        return new SendBodyChunkMessage(length, bytes);
    }
}
