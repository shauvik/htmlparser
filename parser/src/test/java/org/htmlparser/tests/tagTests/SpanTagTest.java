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

import org.htmlparser.Node;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tests.ParserTestCase;

public class SpanTagTest extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.SpanTagTest", "SpanTagTest");
    }

    private static final String HTML_WITH_SPAN =
        "<TD BORDER=\"0.0\" VALIGN=\"Top\" COLSPAN=\"4\" WIDTH=\"33.33%\">" +
        "   <DIV>" +
        "       <SPAN>Flavor: small(90 to 120 minutes)<BR /></SPAN>" +
        "       <SPAN>The short version of our Refactoring Challenge gives participants a general feel for the smells in the code base and includes time for participants to find and implement important refactorings.&#013;<BR /></SPAN>" +
        "   </DIV>" +
        "</TD>";

    public SpanTagTest (String name)
    {
        super(name);
    }
    
    public void testScan() throws Exception {
        createParser(
            HTML_WITH_SPAN
        );
        parser.setNodeFactory (
            new PrototypicalNodeFactory (
                new Tag[] {
                    new TableColumn (),
                    new Span (),
                }));
        parseAndAssertNodeCount(1);
        assertType("node",TableColumn.class,node[0]);
        TableColumn col = (TableColumn)node[0];
        Node spans [] = col.searchFor(Span.class, true).toNodeArray();
        assertEquals("number of spans found",2,spans.length);
        assertStringEquals(
            "span 1",
            "Flavor: small(90 to 120 minutes)",
            spans[0].toPlainTextString()
        );
        assertStringEquals(
            "span 2",
            "The short version of our Refactoring Challenge gives participants a general feel for the smells in the code base and includes time for participants to find and implement important refactorings.&#013;",
            spans[1].toPlainTextString()
        );

    }
}
