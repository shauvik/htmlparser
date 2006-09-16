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

import java.util.Hashtable;
import junit.framework.TestSuite;
import org.htmlparser.Node;

import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

public class BodyTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.BodyTagTest", "BodyTagTest");
    }

    private BodyTag bodyTag;
    private String html = "<body>Yahoo!</body>";

    public BodyTagTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createParser("<html><head><title>body tag test</title></head>" + html + "</html>");
        parseAndAssertNodeCount(1);
        assertTrue("Only node should be an HTML node",node[0] instanceof Html);
        Html html = (Html)node[0];
        assertTrue("HTML node should have two children",2 == html.getChildCount ());
        assertTrue("Second node should be an BODY tag",html.getChild(1) instanceof BodyTag);
        bodyTag = (BodyTag)html.getChild(1);
    }

    public void testToPlainTextString() throws ParserException {
        // check the label node
        assertEquals("Body","Yahoo!",bodyTag.toPlainTextString());
    }

    public void testToHTML() throws ParserException {
        assertStringEquals("Raw String", html, bodyTag.toHtml());
    }

    public void testAttributes ()
    {
        NodeIterator iterator;
        Node node;

        try
        {
            createParser("<body style=\"margin-top:4px; margin-left:20px;\" title=\"body\">");
            parser.setNodeFactory (new PrototypicalNodeFactory (new BodyTag ()));
            iterator = parser.elements ();
            node = null;
            while (iterator.hasMoreNodes ())
            {
                node = iterator.nextNode ();
                if (node instanceof BodyTag)
                {
                    assertNotNull ("no style attribute", ((BodyTag)node).getAttribute ("STYLE"));
                    assertNotNull ("no title attribute", ((BodyTag)node).getAttribute ("TITLE"));
                }
                else
                    fail ("not a body tag");
                assertTrue ("more than one node", !iterator.hasMoreNodes ());
            }
            assertNotNull ("no elements", node);
        }
        catch (ParserException pe)
        {
            fail ("exception thrown " + pe.getMessage ());
        }
    }

    public void testSimpleBody() throws ParserException {
        createParser("<html><head><title>Test 1</title></head><body>This is a body tag</body></html>");
        parser.setNodeFactory (
            new PrototypicalNodeFactory (
                new Tag[]
                {
                    new BodyTag (),
                    new TitleTag (),
                }));
        parseAndAssertNodeCount(6);
        assertTrue(node[4] instanceof BodyTag);
        // check the body node
        BodyTag bodyTag = (BodyTag) node[4];
        assertEquals("Body","This is a body tag",bodyTag.getBody());
        assertEquals("Body","<body>This is a body tag</body>",bodyTag.toHtml());
    }

    public void testBodywithJsp() throws ParserException {
        String body = "<body><%=BodyValue%></body>";
        createParser("<html><head><title>Test 1</title></head>" + body + "</html>");
        parser.setNodeFactory (new PrototypicalNodeFactory (new BodyTag ()));
        parseAndAssertNodeCount(8);
        assertTrue(node[6] instanceof BodyTag);
        // check the body node
        BodyTag bodyTag = (BodyTag) node[6];
        assertStringEquals("Body",body,bodyTag.toHtml());
    }

    public void testBodyMixed() throws ParserException {
        String body = "<body>before jsp<%=BodyValue%>after jsp</body>";
        createParser("<html><head><title>Test 1</title></head>" + body + "</html>");
        parser.setNodeFactory (
            new PrototypicalNodeFactory (
                new Tag[]
                {
                    new BodyTag (),
                    new TitleTag (),
                }));
        parseAndAssertNodeCount(6);
        assertTrue(node[4] instanceof BodyTag);
        // check the body node
        BodyTag bodyTag = (BodyTag) node[4];
        assertEquals("Body",body,bodyTag.toHtml());
    }

    public void testBodyEnding() throws ParserException {
        String body = "<body>before jsp<%=BodyValue%>after jsp";
        createParser("<html>" + body + "</html>");
        parser.setNodeFactory (new PrototypicalNodeFactory (new BodyTag ()));
        parseAndAssertNodeCount(3);
        assertTrue(node[1] instanceof BodyTag);
        // check the body node
        BodyTag bodyTag = (BodyTag) node[1];
        assertEquals("Body",body + "</body>",bodyTag.toHtml());
    }

    public static TestSuite suite()
    {
        return new TestSuite(BodyTagTest.class);
    }
}
