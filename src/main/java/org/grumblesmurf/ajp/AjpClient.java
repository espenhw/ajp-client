package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AjpClient
{
    private final Socket socket;
    
    private AjpClient(Socket socket) {
        this.socket = socket;
    }

    public void close() throws IOException {
        socket.close();
    }

    public AjpResponse get(URL url) {
        return query(url, AjpMethod.GET, null);
    }

    public AjpResponse post(URL url, byte[] content) {
        return query(url, AjpMethod.POST, content);
    }

    public AjpResponse query(URL url, AjpMethod method, byte[] content) {
        try {
            AjpMessage m = new ForwardRequestMessage(url, method, content != null ? content.length : 0);
            m.writeTo(socket.getOutputStream());

            // XXX: YUCK!
            SendHeadersMessage headers = null;
            List<SendBodyChunkMessage> body = new LinkedList<SendBodyChunkMessage>();

            int contentOffset = 0;
            if (content != null) {
                contentOffset += writeBodyChunk(content, Integer.MAX_VALUE, contentOffset);
            }
            
            // XXX: DOUBLEYUCK!
            for (AjpMessage reply = null; !(reply instanceof EndResponseMessage); ) {
                reply = AjpReader.readReplyMessage(socket.getInputStream());
                if (reply instanceof GetBodyChunkMessage) {
                    if (content == null) {
                        throw new AjpException("Server expects request body but there is none to send");
                    }
                    int requestedLength = ((GetBodyChunkMessage)reply).length;
                    contentOffset += writeBodyChunk(content, requestedLength, contentOffset);
                }
                if (reply instanceof SendBodyChunkMessage)
                    body.add((SendBodyChunkMessage)reply);
                if (reply instanceof SendHeadersMessage)
                    headers = (SendHeadersMessage)reply;
            }
            return new AjpResponse(headers, body);
        } catch (IOException e) {
            // FIXME: Uhm
            e.printStackTrace();
            return null;
        }
    }

    private int writeBodyChunk(byte[] content, int requestedLength, int contentOffset) throws IOException {
        int lengthToWrite = Math.min(requestedLength, Math.min((content.length - contentOffset), 8186));

        // XXX: TRIPLEYUCK!
        OutputStream out = socket.getOutputStream();
        out.write(AbstractAjpMessage.AJP_TAG, 0, AbstractAjpMessage.AJP_TAG.length);
        System.err.println("Writing " + lengthToWrite + " bytes");
        if (lengthToWrite > 0) {
            out.write((lengthToWrite + 2 & 0xff00) >> 8);
            out.write(lengthToWrite + 2 & 0x00ff);
            out.write((lengthToWrite & 0xff00) >> 8);
            out.write(lengthToWrite & 0x00ff);
            out.write(content, contentOffset, lengthToWrite - contentOffset);
            out.flush();
            return lengthToWrite;
        } else {
            out.write(0);
            out.write(0);
            out.flush();
            return 0;
        }
    }
    
    public boolean cping() {
        try {
            AjpMessage m = new CPingMessage();
            m.writeTo(socket.getOutputStream());

            AjpMessage reply = AjpReader.readReplyMessage(socket.getInputStream());
            if (reply instanceof CPongMessage) {
                return true;
            } else {
                // FIXME: System.err, dude?
                System.err.printf("Unexpected message: %s", reply);
                // Read valid but unexpected message, WTF?
                return false;
            }
        } catch (IOException e) {
            // FIXME: Uhm
            e.printStackTrace();
            
            // Either an error occurred or we read garbage
            // TODO: We should log here
            return false;
        }
    }

    public static AjpClient newInstance(Socket socket) {
        return new AjpClient(socket);
    }

    public static AjpClient newInstance(String host, int port)
        throws UnknownHostException, IOException {
        return newInstance(new Socket(host, port));
    }
}
