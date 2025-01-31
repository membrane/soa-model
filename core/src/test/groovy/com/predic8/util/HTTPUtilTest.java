package com.predic8.util;

import junit.framework.*;

public class HTTPUtilTest extends TestCase {

    public void testNormalize() {
        assertEquals("file://foo/bar", HTTPUtil.normalize("file:/foo/bar"));
        assertEquals("file://foo/bar", HTTPUtil.normalize("file://foo/bar"));
        assertEquals("file://bar", HTTPUtil.normalize("file:////bar"));
    }
}