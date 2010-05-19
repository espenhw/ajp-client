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
