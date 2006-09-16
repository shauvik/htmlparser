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
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class BaseHrefTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.BaseHrefTagTest", "BaseHrefTagTest");
    }

    public BaseHrefTagTest(String name) {
        super(name);
    }

    public void testConstruction() {
        BaseHrefTag baseRefTag = new BaseHrefTag ();
        baseRefTag.setBaseUrl ("http://www.abc.com");
        assertEquals("Expected Base URL","http://www.abc.com",baseRefTag.getBaseUrl());
    }

    public void testScan() throws ParserException{
        createParser("<html><head><TITLE>test page</TITLE><BASE HREF=\"http://www.abc.com/\"><a href=\"home.cfm\">Home</a>...</html>","http://www.google.com/test/index.html");
        parser.setNodeFactory (
            new PrototypicalNodeFactory (
                new Tag[]
                {
                    new TitleTag (),
                    new LinkTag (),
                    new BaseHrefTag (),
                }));
        parseAndAssertNodeCount(7);
        assertTrue("Base href tag should be the 4th tag", node[3] instanceof BaseHrefTag);
        BaseHrefTag baseRefTag = (BaseHrefTag)node[3];
        assertEquals("Base HREF Url","http://www.abc.com/",baseRefTag.getBaseUrl());
    }

    public void testNotHREFBaseTag() throws ParserException
    {
        String html = "<base target=\"_top\">";
        createParser(html);
        parseAndAssertNodeCount(1);
        assertTrue("Should be a base tag but was "+node[0].getClass().getName(),node[0] instanceof BaseHrefTag);
        BaseHrefTag baseTag = (BaseHrefTag)node[0];
        assertStringEquals("Base Tag HTML", html, baseTag.toHtml());
    }

}
