package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;

import java.util.LinkedList;
import java.util.List;

class ForwardRequestMessage
    extends AbstractAjpMessage
{
    private AjpMethod method = AjpMethod.GET;
    private String protocol = "HTTP/1.0";
    private String requestUri;
    private String remoteAddress = "127.0.0.1";
    private String remoteHost = "localhost";
    private String serverName;
    private int serverPort;
    private boolean isSsl = false;
    private final List<Pair<String, String>> headers = new LinkedList<Pair<String, String>>();
    private final List<Pair<String, String>> attributes = new LinkedList<Pair<String, String>>();
    
    ForwardRequestMessage() {
        super(Constants.PACKET_TYPE_FORWARD_REQUEST);
    }

    ForwardRequestMessage(URL url) {
        this(url, AjpMethod.GET, 0);
    }

    ForwardRequestMessage(URL url, AjpMethod method, int contentLength) {
        this();
        setMethod(method);
        setServerName(url.getHost());

        addHeader("Content-Length", String.valueOf(contentLength));
        if (contentLength > 0) {
            addHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        if (url.getPort() == -1) {
            setServerPort(url.getDefaultPort());
        } else {
            setServerPort(url.getPort());
        }
        
        setRequestUri(url.getPath());
        
        if (url.getQuery() != null) {
            addAttribute(Constants.ATTRIBUTE_QUERY_STRING, url.getQuery());
        }
    }

    final void setMethod(AjpMethod method) {
        this.method = method;
    }

    final void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    final void setServerName(String serverName) {
        this.serverName = serverName;
        addHeader("Host", serverName);
    }

    final void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    final void addHeader(String name, String value) {
        headers.add(Pair.make(name, value));
    }

    final void addAttribute(String name, String value) {
        attributes.add(Pair.make(name, value));
    }

    @Override
    public final void writeTo(OutputStream out) throws IOException {
        writeByte(method.code);
        writeString(protocol);
        writeString(requestUri);
        writeString(remoteAddress);
        writeString(remoteHost);
        writeString(serverName);
        writeInt(serverPort);
        writeBoolean(isSsl);
        writeInt(headers.size());
        writeHeaders(headers);
        writeAttributes(attributes);
        writeByte(Constants.REQUEST_TERMINATOR);
        super.writeTo(out);
    }

    private void writeHeaders(List<Pair<String, String>> headers) {
        for (Pair<String, String> header : headers) {
            String name = header.a;
            String value = header.b;

            if (Constants.COMMON_HEADERS.containsKey(name.toLowerCase())) {
                writeInt(Constants.COMMON_HEADERS.get(name.toLowerCase()));
            } else {
                writeString(name);
            }
            writeString(value);
        }
    }

    private void writeAttributes(List<Pair<String, String>> attributes) {
        for (Pair<String, String> attribute : attributes) {
            String name = attribute.a;
            String value = attribute.b;

            if (Constants.COMMON_ATTRIBUTES.containsKey(name)) {
                writeInt(Constants.COMMON_ATTRIBUTES.get(name));
                writeString(value);
            } else {
                writeInt(Constants.ATTRIBUTE_GENERIC);
                writeString(name);
                writeString(value);
            }
        }
    }
}
