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
import org.htmlparser.tags.FrameSetTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class FrameSetTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.FrameSetTagTest", "FrameSetTagTest");
    }

    public FrameSetTagTest(String name) {
        super(name);
    }

    public void testToHTML() throws ParserException{
        String html = "<frameset rows=\"115,*\" frameborder=\"NO\" border=\"0\" framespacing=\"0\">\n"+
            "<frame name=\"topFrame\" noresize src=\"demo_bc_top.html\" scrolling=\"NO\" frameborder=\"NO\">\n"+
            "<frame name=\"mainFrame\" src=\"http://www.kizna.com/web_e/\" scrolling=\"AUTO\">\n"+
        "</frameset>";
        createParser(html);
        parseAndAssertNodeCount(1);
        assertTrue("Node 0 should be a FrameSetTag",node[0] instanceof FrameSetTag);
        FrameSetTag frameSetTag = (FrameSetTag)node[0];
        assertStringEquals("HTML Contents", html, frameSetTag.toHtml());
    }

    public void testScan() throws ParserException {
        createParser(
        "<frameset rows=\"115,*\" frameborder=\"NO\" border=\"0\" framespacing=\"0\">\n"+
            "<frame name=\"topFrame\" noresize src=\"demo_bc_top.html\" scrolling=\"NO\" frameborder=\"NO\">\n"+
            "<frame name=\"mainFrame\" src=\"http://www.kizna.com/web_e/\" scrolling=\"AUTO\">\n"+
        "</frameset>","http://www.google.com/test/index.html");

        parser.setNodeFactory (
            new PrototypicalNodeFactory (
                new Tag[]
                {
                    new FrameSetTag (),
                    new FrameTag (),
                }));
        parseAndAssertNodeCount(1);
        assertTrue("Node 0 should be End Tag",node[0] instanceof FrameSetTag);
        FrameSetTag frameSetTag = (FrameSetTag)node[0];
        // Find the details of the frameset itself
        assertEquals("Rows","115,*",frameSetTag.getAttribute("rows"));
        assertEquals("FrameBorder","NO",frameSetTag.getAttribute("FrameBorder"));
        assertEquals("FrameSpacing","0",frameSetTag.getAttribute("FrameSpacing"));
        assertEquals("Border","0",frameSetTag.getAttribute("Border"));
        // Now check the frames
        FrameTag topFrame = frameSetTag.getFrame("topFrame");
        FrameTag mainFrame = frameSetTag.getFrame("mainFrame");
        assertNotNull("Top Frame should not be null",topFrame);
        assertNotNull("Main Frame should not be null",mainFrame);
        assertEquals("Top Frame Name","topFrame",topFrame.getFrameName());
        assertEquals("Top Frame Location","http://www.google.com/test/demo_bc_top.html",topFrame.getFrameLocation());
        assertEquals("Main Frame Name","mainFrame",mainFrame.getFrameName());
        assertEquals("Main Frame Location","http://www.kizna.com/web_e/",mainFrame.getFrameLocation());
        assertEquals("Scrolling in Main Frame","AUTO",mainFrame.getAttribute("Scrolling"));
    }
}

