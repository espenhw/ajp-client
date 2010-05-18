package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

class GetBodyChunkMessage
    extends AbstractAjpReplyMessage
{
    final int length;

    GetBodyChunkMessage(int length) {
        this.length = length;
    }

    static GetBodyChunkMessage readFrom(InputStream in) throws IOException {
        int length = AjpReader.readInt(in);
        return new GetBodyChunkMessage(length);
    }
}
