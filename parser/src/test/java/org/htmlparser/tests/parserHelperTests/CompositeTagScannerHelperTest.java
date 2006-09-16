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

package org.htmlparser.tests.parserHelperTests;

import org.htmlparser.Tag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

/**
 * @author Somik Raha
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CompositeTagScannerHelperTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.parserHelperTests.CompositeTagScannerHelperTest", "CompositeTagScannerHelperTest");
    }

    public CompositeTagScannerHelperTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    public void testIsXmlEndTagForRealXml () throws ParserException
    {
        String html = "<something/>";
        createParser (html);
        parseAndAssertNodeCount (1);
        assertTrue("should be a tag", node[0] instanceof Tag);
        assertTrue("should be an xml end tag", ((Tag)node[0]).isEmptyXmlTag ());
    }

    public void testIsXmlEndTagForFalseMatches () throws ParserException
    {
        String html = "<a href=http://someurl.com/>";
        createParser (html);
        parseAndAssertNodeCount (1);
        assertTrue("should be a tag", node[0] instanceof Tag);
        assertTrue("should not be an xml end tag", !((Tag)node[0]).isEmptyXmlTag ());
    }
}
