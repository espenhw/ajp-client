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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AjpResponse
{
    private final int statusCode;
    private final String message;
    private final Map<String, List<String>> headers;
    private final byte[] content;

    AjpResponse(SendHeadersMessage headers, List<SendBodyChunkMessage> body) {
        statusCode = headers.statusCode;
        message = headers.statusMessage;

        Map<String, List<String>> tmpHeaders = new HashMap<String, List<String>>();
        for (Pair<String,String> h : headers.headers) {
            if (!tmpHeaders.containsKey(h.a)) {
                tmpHeaders.put(h.a, new LinkedList<String>());
            }
            tmpHeaders.get(h.a).add(h.b);
        }

        for (Map.Entry<String, List<String>> e : tmpHeaders.entrySet()) {
            e.setValue(Collections.unmodifiableList(e.getValue()));
        }
        
        this.headers = Collections.unmodifiableMap(tmpHeaders);

        int totalLength = 0;
        for (SendBodyChunkMessage m : body) {
            totalLength += m.length;
        }
        content = new byte[totalLength];
        int offset = 0;
        for (SendBodyChunkMessage m : body) {
            System.arraycopy(m.bytes, 0, content, offset, m.length);
            offset += m.length;
        }
    }
        
    public final int getResponseCode() {
        return statusCode;
    }

    public final String getResponseMessage() {
        return message;
    }

    public final String getHeaderField(String name) {
        if (headers.containsKey(name)) {
            return headers.get(name).get(0);
        }
        return null;
    }

    public final Map<String, List<String>> getHeaderFields() {
        return headers;
    }

    public final int getContentLength() {
        return content.length;
    }

    public final String getContentType() {
        return getHeaderField("Content-Type");
    }

    public final byte[] getContent() {
        return Arrays.copyOf(content, content.length);
    }
}
