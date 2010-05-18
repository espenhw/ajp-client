package org.grumblesmurf.ajp;

import java.io.IOException;

class CPingMessage
    extends AbstractAjpMessage
{
    CPingMessage() throws IOException {
        super(Constants.PACKET_TYPE_CPING); 
    }
}
