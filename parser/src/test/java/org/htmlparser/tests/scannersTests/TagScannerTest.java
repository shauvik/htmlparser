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

package org.htmlparser.tests.scannersTests;

import org.htmlparser.Tag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;

public class TagScannerTest extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.scannersTests.TagScannerTest", "TagScannerTest");
    }

    public TagScannerTest(String name) {
        super(name);
    }

    public void testTagExtraction() throws ParserException
    {
        String testHTML = "<AREA \n coords=0,0,52,52 href=\"http://www.yahoo.com/r/c1\" shape=RECT>";
        createParser(testHTML);
        Tag tag = (Tag)parser.elements ().nextNode ();
        assertNotNull(tag);
    }

    public void testRemoveChars() {
        String test = "hello\nworld\n\tqsdsds";
        String result = ParserUtils.removeChars(test,'\n');
        assertEquals("Removing Chars","helloworld\tqsdsds",result);
    }
}
