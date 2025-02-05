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

import org.junit.Assume
import org.junit.Before
import org.junit.Test

import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class ExternalResolverTest {

    def resolver
    def url

    @Before
    void setUp() {
        resolver = new ExternalResolver()
        url = 'https://www.predic8.de/city-service?wsdl'
    }

    @Test
    void testResolveAsString() {
        Assume.assumeTrue(!System.getenv('OFFLINETESTING'))
        assert resolver.resolveAsString(url) != null
    }

    @Test
    void fileUriWithSpaces() {
        def is = resolver.resolve(this.getClass().getResource("/").toString() + "resource with spaces.xml", "")
        assert readFromInputStream(is).equals("<foo/>")
    }

    private String readFromInputStream(is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        reader.lines().collect(Collectors.joining(System.lineSeparator()))
    }
}
