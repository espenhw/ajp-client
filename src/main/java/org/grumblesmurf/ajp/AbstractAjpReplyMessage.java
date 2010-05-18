package org.grumblesmurf.ajp;

import java.io.OutputStream;

abstract class AbstractAjpReplyMessage
    implements AjpMessage
{
    public final void writeTo(OutputStream out) {
        throw new UnsupportedOperationException("Cannot write replies yet");
    }

    public final byte[] bytes() {
        throw new UnsupportedOperationException("Cannot write replies yet");
    }
}
