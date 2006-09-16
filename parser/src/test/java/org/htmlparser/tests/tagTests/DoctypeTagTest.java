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

package org.htmlparser.tests.tagTests;

import org.htmlparser.Node;
import org.htmlparser.tags.DoctypeTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

public class DoctypeTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.DoctypeTagTest", "DoctypeTagTest");
    }

    public DoctypeTagTest(String name) {
        super(name);
    }

    public void testToHTML() throws ParserException
    {
        String testHTML = new String(
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">\n"+
        "<HTML>\n"+
        "<HEAD>\n"+
        "<TITLE>Cogs of Chicago</TITLE>\n"+
        "</HEAD>\n"+
        "<BODY>\n"+
        "...\n"+
        "</BODY>\n"+
        "</HTML>\n");
        createParser(testHTML);
        parseAndAssertNodeCount(4);
        // The first node should be an DoctypeTag
        assertTrue("First node should be a DoctypeTag",node[0] instanceof DoctypeTag);
        DoctypeTag docTypeTag = (DoctypeTag)node[0];
        assertStringEquals("toHTML()","<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">",docTypeTag.toHtml());
    }

    /**
     * See bug #833592 DOCTYPE element is not parsed correctly
     * Contributed by Trevor Watson (t007).
     */
    public void DocTypeElementTest () throws ParserException
    {
        final String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
        final String HTML = DOCTYPE + "\n<HTML>\n  <HEAD>\n    <TITLE>HTMLParserDocTypeBugTest</TITLE>\n  </HEAD>\n  <BODY>\n    HTMLParser DOCTYPE node bug test.\n  </BODY>\n</HTML>";

        createParser(HTML);

        NodeIterator e = parser.elements();
        Node node = e.nextNode();

        // First node is doctype
        assertStringEquals("Doctype element output is incorrect.", DOCTYPE, node.toHtml());
    }
}
