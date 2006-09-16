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

import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.visitors.NodeVisitor;

public class NodeVisitorTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.visitorsTests.NodeVisitorTest", "NodeVisitorTest");
    }

    public NodeVisitorTest(String name) {
        super(name);
    }

    public void testVisitTag() throws Exception {
        ParameterVisitor visitor = new ParameterVisitor();
        createParser(
            "<input>" +
                "<param name='key1'>value1</param>"+
                "<param name='key2'>value2</param>"+
            "</input>"
        );
        parser.visitAllNodesWith(visitor);
        assertEquals("value of key1","value1",visitor.getValue("key1"));
        assertEquals("value of key2","value2",visitor.getValue("key2"));
    }

    class ParameterVisitor extends NodeVisitor {
        Map paramsMap = new HashMap();
        String lastKeyVisited;

        public String getValue(String key) {
            return (String)paramsMap.get(key);
        }

        public void visitStringNode(Text stringNode) {
            paramsMap.put(lastKeyVisited,stringNode.getText());
        }

        public void visitTag(Tag tag) {
            if (tag.getTagName().equals("PARAM")) {
                lastKeyVisited = tag.getAttribute("NAME");
            }
        }
    }
}
