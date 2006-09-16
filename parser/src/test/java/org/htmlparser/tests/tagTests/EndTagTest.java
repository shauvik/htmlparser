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

import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class EndTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.EndTagTest", "EndTagTest");
    }

    public EndTagTest(String name) {
        super(name);
    }

    public void testToHTML() throws ParserException {
        createParser("<HTML></HTML>");
        parser.setNodeFactory (new PrototypicalNodeFactory (true));
        parseAndAssertNodeCount(2);
        // The node should be a tag
        assertTrue("Node should be a Tag",node[1] instanceof Tag);
        Tag endTag = (Tag)node[1];
        assertTrue("Node should be an end Tag",endTag.isEndTag ());
        assertEquals("Raw String","</HTML>",endTag.toHtml());
    }

    public void testEndTagFind() throws ParserException {
        String testHtml =
            "<SCRIPT>document.write(d+\".com\")</SCRIPT><BR>";
        createParser(testHtml);
        parser.setNodeFactory (new PrototypicalNodeFactory (true));
        int pos = testHtml.indexOf("</SCRIPT>");
        parseAndAssertNodeCount(4);
        assertTrue("Node should be a Tag",node[2] instanceof Tag);
        Tag endTag = (Tag)node[2];
        assertTrue("Node should be an end Tag",endTag.isEndTag ());
        assertEquals("endtag element begin",pos,endTag.getStartPosition ());
        assertEquals("endtag element end",pos+9,endTag.getEndPosition ());
    }
}
