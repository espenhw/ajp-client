package org.grumblesmurf.ajp;

import java.io.ByteArrayInputStream;
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
        byte[] bytes = new byte[length - 1];
        fullyRead(bytes, in);

        switch (type) {
        case Constants.PACKET_TYPE_CPONG:
            return new CPongMessage();
        case Constants.PACKET_TYPE_SEND_HEADERS:
            return SendHeadersMessage.readFrom(new ByteArrayInputStream(bytes));
        case Constants.PACKET_TYPE_SEND_BODY_CHUNK:
            return SendBodyChunkMessage.readFrom(new ByteArrayInputStream(bytes));
        case Constants.PACKET_TYPE_END_RESPONSE:
            return EndResponseMessage.readFrom(new ByteArrayInputStream(bytes));
        default:
            // FIXME: This just consumes the data
            System.err.printf("Unknown packet type %x%n", type);
            System.err.println(new String(bytes, "ISO-8859-1"));
            return null;
        }
    }

    static int readInt(InputStream in) throws IOException {
        byte[] buf = new byte[2];
        fullyRead(buf, in);
        return makeInt(buf[0], buf[1]);
    }

    static int makeInt(int b1, int b2) {
        return b1 << 8 | (b2 & 0xff);
    }

    static String readString(InputStream in) throws IOException {
        int len = readInt(in);
        return readString(len, in);
    }
    
    static String readString(int len, InputStream in) throws IOException {
        if (len == -1) {
            // XXX:  Will this ever occur?
            return null;
        }
        byte[] buf = new byte[len];
        fullyRead(buf, in);
        // Skip the terminating \0
        in.read();
        
        // XXX: Encoding?
        return new String(buf, "UTF-8");
    }

    static void fullyRead(byte[] buffer, InputStream in) throws IOException {
        int readLength = in.read(buffer);
        if (readLength != buffer.length) {
            throw new ShortAjpReadException(buffer.length, readLength);
        }
    }

    static int readByte(InputStream in) throws IOException {
        return in.read();
    }
    
    static boolean readBoolean(InputStream in) throws IOException {
        return readByte(in) > 0;
    }
    
    private static void consume(int expected, InputStream in) throws IOException {
        int readByte = readByte(in);
        if (readByte != expected) {
            throw new UnexpectedAjpByteException(expected, readByte);
        }
    }
}
