/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.xml.util

import java.io.IOException;
import java.io.InputStream;

import com.predic8.soamodel.KnownSchemas;

abstract class ResourceResolver {

    Map knownDocs = KnownSchemas.docs

    protected InputStream fixUtf(InputStream is) throws IOException {
        PushbackInputStream pis = new PushbackInputStream(new BufferedInputStream(is), 3)
        byte[] bom = new byte[3]
        if (pis.read(bom) != -1) {
            if (isBOM(bom)) {
                return pis
            }

            if (isReplacementCharacter(bom)) {
                pis.read(bom)

                if (!isReplacementCharacter(bom)) {
                    pis.unread(bom)
                }

                return pis
            }

            pis.unread(bom)
        }
        pis
    }

    private boolean isBOM(byte[] bom) {
        bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF
    }

    private boolean isReplacementCharacter(byte[] bom) {
        bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBF && bom[2] == (byte) 0xBD
    }
}