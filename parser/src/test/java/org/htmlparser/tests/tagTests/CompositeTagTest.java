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
import org.htmlparser.Text;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;


public class CompositeTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.CompositeTagTest", "CompositeTagTest");
    }

    public CompositeTagTest(String name) {
        super(name);
    }

    public void testDigupStringNode() throws ParserException {
        createParser(
            "<table>" +
                "<table>" +
                    "<tr>" +
                    "<td>" +
                    "Hello World" +
                    "</td>" +
                    "</tr>" +
                "</table>" +
            "</table>"
        );
        parseAndAssertNodeCount(1);
        TableTag tableTag = (TableTag)node[0];
        Text[] stringNode =
            tableTag.digupStringNode("Hello World");

        assertEquals("number of string nodes",1,stringNode.length);
        assertNotNull("should have found string node",stringNode);
        Node parent = stringNode[0].getParent();
        assertType("should be column",TableColumn.class,parent);
        parent = parent.getParent();
        assertType("should be row",TableRow.class,parent);
        parent = parent.getParent();
        assertType("should be table",TableTag.class,parent);
        parent = parent.getParent();
        assertType("should be table again",TableTag.class,parent);
        assertSame("should be original table",tableTag,parent);
    }

    public void testFindPositionOf() throws ParserException {
        createParser(
            "<table>" +
                "<table>" +
                    "<tr>" +
                    "<td>" +
                    "Hi There<a><b>sdsd</b>" +
                    "Hello World" +
                    "</td>" +
                    "</tr>" +
                "</table>" +
            "</table>"
        );
        parseAndAssertNodeCount(1);
        TableTag tableTag = (TableTag)node[0];
        Text [] stringNode =
            tableTag.digupStringNode("Hello World");

        assertEquals("number of string nodes",1,stringNode.length);
        assertNotNull("should have found string node",stringNode);
        CompositeTag parent = (CompositeTag)stringNode[0].getParent();
        int pos = parent.findPositionOf(stringNode[0]);
        /* a(b(),string("sdsd"),/b(),string("Hello World")) */
        /*   0   1              2    3 */
        assertEquals("position",3,pos);
    }
}
