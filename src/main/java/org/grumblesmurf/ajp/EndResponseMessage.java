package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

class EndResponseMessage
    extends AbstractAjpReplyMessage
{
    final boolean reuse;

    EndResponseMessage(boolean reuse) {
        this.reuse = reuse;
    }

    @Override
    public String toString() {
        return String.format("END (%sreuse)", reuse ? "" : "don't ");
    }

    static EndResponseMessage readFrom(InputStream in) throws IOException {
        return new EndResponseMessage(AjpReader.readBoolean(in));
    }
}
