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

import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.tags.Bullet;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

public class BulletTagTest extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.BulletTagTest", "BulletTagTest");
    }

    public BulletTagTest (String name)
    {
        super(name);
    }
    
    public void testBulletFound() throws Exception {
        createParser(
            "<LI><A HREF=\"collapseHierarchy.html\">Collapse Hierarchy</A>\n"+
            "</LI>"
        );
        parseAndAssertNodeCount(1);
        assertType("should be a bullet",Bullet.class,node[0]);
    }


    public void testOutOfMemoryBug() throws ParserException {
        createParser(
            "<html>" +
            "<head>" +
            "<title>Foo</title>" +
            "</head>" +
            "<body>" +
            "    <ul>" +
            "        <li>" +
            "            <a href=\"http://foo.com/c.html\">bibliographies on:" +
            "                <ul>" +
            "                    <li>chironomidae</li>" +
            "                </ul>" +
            "            </a>" +
            "        </li>" +
            "    </ul>" +
            "" +
            "</body>" +
            "</html>"
        );
        for (NodeIterator i = parser.elements();i.hasMoreNodes();)
            i.nextNode();
    }

    public void testNonEndedBullets() throws ParserException {
        createParser(
            "<li>forest practices legislation penalties for non-compliance\n"+
            " (Kwan)  <A HREF=\"/hansard/37th3rd/h21107a.htm#4384\">4384-5</A>\n"+
            "<li>passenger rail service\n"+
            " (MacPhail)  <A HREF=\"/hansard/37th3rd/h21021p.htm#3904\">3904</A>\n"+
            "<li>referendum on principles for treaty negotiations\n"+
            " (MacPhail)  <A HREF=\"/hansard/37th3rd/h20313p.htm#1894\">1894</A>\n"+
            "<li>transportation infrastructure projects\n"+
            " (MacPhail)  <A HREF=\"/hansard/37th3rd/h21022a.htm#3945\">3945-7</A>\n"+
            "<li>tuition fee freeze"
        );
        parseAndAssertNodeCount(5);
        for (int i=0;i<nodeCount;i++) {
            assertType("node "+i,Bullet.class,node[i]);
        }
    }
}
