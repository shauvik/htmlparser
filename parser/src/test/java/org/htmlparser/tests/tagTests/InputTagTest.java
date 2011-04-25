// HTMLParser Library - A java-based parser for HTML
// http://htmlparser.org
// Copyright (C) 2006 Dhaval Udani
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

import org.htmlparser.tags.InputTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class InputTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.InputTagTest", "InputTagTest");
    }

    public InputTagTest(String name)
    {
        super(name);
    }

    public void testToHTML() throws ParserException
    {
        String testHTML = "<INPUT type=\"text\" name=\"Google\">";
        createParser(testHTML);
        parseAndAssertNodeCount(1);
        assertTrue("Node 1 should be INPUT Tag",node[0] instanceof InputTag);
        InputTag InputTag;
        InputTag = (InputTag) node[0];
        assertStringEquals ("HTML String",testHTML,InputTag.toHtml());
    }

    /**
     * Reproduction of bug report 663038
     * @throws ParserException
     */
    public void testToHTML2() throws ParserException
    {
        String testHTML ="<INPUT type=\"checkbox\" "
            +"name=\"cbCheck\" checked>";
        createParser(testHTML);
        parseAndAssertNodeCount(1);
        assertTrue("Node 1 should be INPUT Tag",
            node[0] instanceof InputTag);
        InputTag InputTag;
        InputTag = (InputTag) node[0];
        assertStringEquals("HTML String", testHTML, InputTag.toHtml());
    }

    public void testScan() throws ParserException
    {
        createParser("<INPUT type=\"text\" name=\"Google\">","http://www.google.com/test/index.html");
        parseAndAssertNodeCount(1);
        assertTrue(node[0] instanceof InputTag);

        // check the input node
        InputTag inputTag = (InputTag) node[0];
        assertEquals("Type","text",inputTag.getAttribute("TYPE"));
        assertEquals("Name","Google",inputTag.getAttribute("NAME"));
    }
}
