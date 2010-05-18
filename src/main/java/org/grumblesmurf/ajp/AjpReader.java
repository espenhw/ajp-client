package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.InputStream;

final class AjpReader
{
    private AjpReader() {
    }

    static AjpMessage readReplyMessage(InputStream in) throws IOException {
        consume('A', in);
        consume('B', in);
        int length = readInt(in);
        int type = in.read();

        switch (type) {
        case Constants.PACKET_TYPE_CPONG:
            return new CPongMessage();
        }
        
        // FIXME: This just consumes the data
        byte[] bytes = new byte[length - 1];
        fullyRead(bytes, in);
        return null;
    }

    private static int readInt(InputStream in) throws IOException {
        byte[] buf = new byte[2];
        fullyRead(buf, in);
        return buf[0] << 8 & buf[1];
    }

    private static void fullyRead(byte[] buffer, InputStream in) throws IOException {
        int readLength = in.read(buffer);
        if (readLength != buffer.length) {
            throw new ShortAjpReadException(buffer.length, readLength);
        }
    }

    private static void consume(int expected, InputStream in) throws IOException {
        int readByte = in.read();
        if (readByte != expected) {
            throw new UnexpectedAjpByteException(expected, readByte);
        }
    }
}
