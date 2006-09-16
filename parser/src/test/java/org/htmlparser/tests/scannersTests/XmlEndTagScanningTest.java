// HTMLParser Library - A java-based parser for HTML
// http://htmlparser.org
// Copyright (C) 2006 Somik Raha
//
// Revision Control Information
//
// $URL$
// $Author$
// $Date$
// $Revision$
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the Common Public License; either
// version 1.0 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// Common Public License for more details.
//
// You should have received a copy of the Common Public License
// along with this library; if not, the license is available from
// the Open Source Initiative (OSI) website:
//   http://opensource.org/licenses/cpl1.0.php

package org.htmlparser.tests.scannersTests;

import org.htmlparser.tags.Div;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class XmlEndTagScanningTest extends ParserTestCase{

    static
    {
        System.setProperty ("org.htmlparser.tests.scannersTests.XmlEndTagScanningTest", "XmlEndTagScanningTest");
    }

    public XmlEndTagScanningTest(String name) {
        super(name);
    }

    public void testSingleTagParsing() throws ParserException {
        createParser("<div style=\"page-break-before: always; \" />");
        parseAndAssertNodeCount(1);
        assertType("div tag",Div.class,node[0]);
        Div div = (Div)node[0];
        assertStringEquals(
            "style",
            "page-break-before: always; ",
            div.getAttribute("style")
        );
    }

}
