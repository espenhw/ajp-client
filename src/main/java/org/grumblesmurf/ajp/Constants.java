package org.grumblesmurf.ajp;

import java.util.HashMap;
import java.util.Map;

final class Constants
{
    static final int PACKET_TYPE_FORWARD_REQUEST = 2;
    static final int PACKET_TYPE_SEND_BODY_CHUNK = 3;
    static final int PACKET_TYPE_SEND_HEADERS = 4;
    static final int PACKET_TYPE_END_RESPONSE = 5;
    static final int PACKET_TYPE_GET_BODY_CHUNK = 6;
    
    static final int PACKET_TYPE_CPONG = 9;
    static final int PACKET_TYPE_CPING = 10;

    static final int REQUEST_TERMINATOR = 0xff;

    static final Map<String, Integer> COMMON_HEADERS = new HashMap<String, Integer>();
    static {
        COMMON_HEADERS.put("accept", 0xA001);
        COMMON_HEADERS.put("accept-charset", 0xA002);
        COMMON_HEADERS.put("accept-encoding", 0xA003);
        COMMON_HEADERS.put("accept-language", 0xA004);
        COMMON_HEADERS.put("authorization", 0xA005);
        COMMON_HEADERS.put("connection", 0xA006);
        COMMON_HEADERS.put("content-type", 0xA007);
        COMMON_HEADERS.put("content-length", 0xA008);
        COMMON_HEADERS.put("cookie", 0xA009);
        COMMON_HEADERS.put("cookie2", 0xA00A);
        COMMON_HEADERS.put("host", 0xA00B);
        COMMON_HEADERS.put("pragma", 0xA00C);
        COMMON_HEADERS.put("referer", 0xA00D);
        COMMON_HEADERS.put("user-agent", 0xA00E);
    }

    static final String ATTRIBUTE_QUERY_STRING = "query_string";
    static final Map<String, Integer> COMMON_ATTRIBUTES = new HashMap<String, Integer>();
    static {
        COMMON_ATTRIBUTES.put("context", 0x01);
        COMMON_ATTRIBUTES.put("servlet_path", 0x02);
        COMMON_ATTRIBUTES.put("remote_user", 0x03);
        COMMON_ATTRIBUTES.put("auth_type", 0x04);
        COMMON_ATTRIBUTES.put(ATTRIBUTE_QUERY_STRING, 0x05);
        COMMON_ATTRIBUTES.put("route", 0x06);
        COMMON_ATTRIBUTES.put("ssl_cert", 0x07);
        COMMON_ATTRIBUTES.put("ssl_cipher", 0x08);
        COMMON_ATTRIBUTES.put("ssl_session", 0x09);
        COMMON_ATTRIBUTES.put("ssl_key_size", 0x0B);
        COMMON_ATTRIBUTES.put("secret", 0x0C);
        COMMON_ATTRIBUTES.put("stored_method", 0x0D);
    }
    static final int ATTRIBUTE_GENERIC = 0x0A;

    static final String[] RESPONSE_HEADERS = { "Content-Type",
                                               "Content-Language",
                                               "Content-Length",
                                               "Date",
                                               "Last-Modified",
                                               "Location",
                                               "Set-Cookie",
                                               "Set-Cookie2",
                                               "Servlet-Engine",
                                               "Status",
                                               "WWW-Authenticate", 
    };
    
    private Constants() {
    }
}
