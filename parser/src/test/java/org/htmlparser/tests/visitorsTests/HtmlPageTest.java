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

package org.htmlparser.tests.visitorsTests;

import org.htmlparser.Node;
import org.htmlparser.Text;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;

public class HtmlPageTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.visitorsTests.HtmlPageTest", "HtmlPageTest");
    }

    private static final String SIMPLE_PAGE =
        "<html>" +
            "<head>" +
                "<title>Welcome to the HTMLParser website</title>" +
            "</head>" +
            "<body>" +
                "Welcome to HTMLParser" +
            "</body>" +
        "</html>";

    private static final String guts = 
        "Welcome to HTMLParser" +
        "<table>" +
            "<tr>" +
                "<td>cell 1</td>" +
                "<td>cell 2</td>" +
            "</tr>" +
        "</table>";

    private static final String PAGE_WITH_TABLE =
        "<html>" +
            "<head>" +
                "<title>Welcome to the HTMLParser website</title>" +
            "</head>" +
            "<body>" +
                guts +
            "</body>" +
        "</html>";

    public HtmlPageTest(String name) {
        super(name);
    }

    public void testCreateSimplePage() throws Exception {
        createParser(
            SIMPLE_PAGE
        );
        HtmlPage page = new HtmlPage(parser);
        parser.visitAllNodesWith(page);
        assertStringEquals(
            "title",
            "Welcome to the HTMLParser website",
            page.getTitle()
        );
        NodeList bodyNodes = page.getBody();
        assertEquals("number of nodes in body",1,bodyNodes.size());
        Node node = bodyNodes.elementAt(0);
        assertTrue("expected stringNode but was "+node.getClass().getName(),
            node instanceof Text
        );
        assertStringEquals(
            "body contents",
            "Welcome to HTMLParser",
            page.getBody().asString()
        );
    }

    public void testCreatePageWithTables() throws Exception {
        createParser(
            PAGE_WITH_TABLE
        );
        HtmlPage page = new HtmlPage(parser);
        parser.visitAllNodesWith(page);
        NodeList bodyNodes = page.getBody();
        assertEquals("number of nodes in body",2,bodyNodes.size());
        assertXmlEquals("body html", guts, bodyNodes.toHtml());
        TableTag tables [] = page.getTables();
        assertEquals("number of tables",1,tables.length);
        assertEquals("number of rows",1,tables[0].getRowCount());
        TableRow row = tables[0].getRow(0);
        assertEquals("number of columns",2,row.getColumnCount());
        TableColumn [] col = row.getColumns();
        assertEquals("column contents","cell 1",col[0].toPlainTextString());
        assertEquals("column contents","cell 2",col[1].toPlainTextString());
    }
}
