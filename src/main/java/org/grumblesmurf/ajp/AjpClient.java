package org.grumblesmurf.ajp;

import java.io.IOException;

import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.Arrays;

public class AjpClient
{
    private final Socket socket;
    
    private AjpClient(Socket socket) {
        this.socket = socket;
    }

    public void close() throws IOException {
        socket.close();
    }

    // FIXME: Return something sensible
    public void get(URL url) {
        try {
            AjpMessage m = new ForwardRequestMessage(url);
            m.writeTo(socket.getOutputStream());

            // FIXME: do something useful
            byte[] buffer = new byte[1024];
            int read;
            while ((read = socket.getInputStream().read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, read, "ISO-8859-1"));
            }
        } catch (IOException e) {
            // FIXME: Uhm
            e.printStackTrace();
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
