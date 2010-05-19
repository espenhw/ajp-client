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

    @Test
    public void makeIntWorks() throws Exception {
        assertThat(AjpReader.makeInt(0, 1), is(1));
        assertThat(AjpReader.makeInt(1, 0), is(256));
    }
}
