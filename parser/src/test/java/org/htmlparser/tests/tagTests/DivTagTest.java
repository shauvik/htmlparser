// HTMLParser Library - A java-based parser for HTML
// http://htmlparser.org
// Copyright (C) 2006 Derrick Oswald
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
import org.htmlparser.tags.Div;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class DivTagTest extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.DivTagTest", "DivTagTest");
    }

    public DivTagTest (String name)
    {
        super(name);
    }
    
    public void testScan() throws ParserException {
        createParser("<table><div align=\"left\">some text</div></table>");
        parseAndAssertNodeCount(1);
        assertType("node should be table",TableTag.class,node[0]);
        TableTag tableTag = (TableTag)node[0];
        Div div = (Div)tableTag.searchFor(Div.class, true).toNodeArray()[0];
        assertEquals("div contents","some text",div.toPlainTextString());
    }

    /**
     * Test case for bug #735193 Explicit tag type recognition for CompositTags not working.
     */
    public void testInputInDiv() throws ParserException
    {
        createParser("<div><INPUT type=\"text\" name=\"X\">Hello</INPUT></div>");
        parser.setNodeFactory (
            new PrototypicalNodeFactory (
                new Tag[]
                {
                    new Div (),
                    new InputTag (),
                }));
        parseAndAssertNodeCount(1);
        assertType("node should be div",Div.class,node[0]);
        Div div = (Div)node[0];
        assertType("child not input",InputTag.class,div.getChild (0));
    }
}
