package org.grumblesmurf.ajp;

import java.io.IOException;

public class AjpException
    extends IOException 
{
    AjpException(String message) {
        super(message);
    }

    AjpException(String message, Throwable cause) {
        super(message, cause);
    }
}

class UnexpectedAjpByteException
    extends AjpException 
{
    UnexpectedAjpByteException(int expected, int actual) {
        super(String.format("Unexpected byte: expected %x, got %x", expected, actual));
    }
}

class ShortAjpReadException
    extends AjpException 
{
    ShortAjpReadException(int expectedLength, int actualLength) {
        super(String.format("Short read: expected %s bytes but got only %s", expectedLength, actualLength));
    }
}
