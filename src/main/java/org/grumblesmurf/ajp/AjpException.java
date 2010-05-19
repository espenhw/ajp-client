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
