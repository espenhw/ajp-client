// Copyright (c) 2010 Espen Wiborg <espenhw@grumblesmurf.org>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
    private String username;
    private String password;
    private List<Pair<String,String>> headers = new LinkedList<Pair<String,String>>();
        
    private AjpClient(Socket socket) {
        this.socket = socket;
    }

    public final void setAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public final void addHeader(String name, String value) {
        headers.add(Pair.make(name, value));
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
            ForwardRequestMessage m = new ForwardRequestMessage(url, method, content != null ? content.length : 0);
            for (Pair<String,String> header : headers) {
                m.addHeader(header.a, header.b);
            }
            
            if (username != null) {
                String auth = String.format("%s:%s", username, password);
                m.addHeader("Authorization", "Basic " + Base64.encode(auth));
            }
                        
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
