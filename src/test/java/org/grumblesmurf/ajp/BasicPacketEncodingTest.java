package org.grumblesmurf.ajp;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicPacketEncodingTest
{
    @Test
    public void cpingMessageIsCorrectLength() throws Exception {
        AjpMessage cping = new CPingMessage();
        byte[] cpingBytes = cping.bytes();
        assertThat(cpingBytes.length, is(5));
    }

    @Test
    public void cpingMessagePayloadLengthIsOne() throws Exception {
        AjpMessage cping = new CPingMessage();
        byte[] cpingBytes = cping.bytes();
        assertThat(cpingBytes[2], is((byte)0));
        assertThat(cpingBytes[3], is((byte)1));
    }
}
