package org.grumblesmurf.ajp;

import java.io.IOException;
import java.io.OutputStream;

interface AjpMessage
{
    byte[] bytes();
    void writeTo(OutputStream out) throws IOException;
}
