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

import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.visitors.TextExtractingVisitor;

public class TextExtractingVisitorTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.visitorsTests.TextExtractingVisitorTest", "TextExtractingVisitorTest");
    }

    public TextExtractingVisitorTest(String name) {
        super(name);
    }

    public void testSimpleVisit() throws Exception {
        createParser("<HTML><HEAD><TITLE>Hello World</TITLE></HEAD></HTML>");
        TextExtractingVisitor visitor = new TextExtractingVisitor();
        parser.visitAllNodesWith(visitor);
        assertStringEquals(
            "extracted text",
            "Hello World",
            visitor.getExtractedText()
        );
    }

    public void testSimpleVisitWithRegisteredScanners() throws Exception {
        createParser("<HTML><HEAD><TITLE>Hello World</TITLE></HEAD></HTML>");
        TextExtractingVisitor visitor = new TextExtractingVisitor();
        parser.visitAllNodesWith(visitor);
        assertStringEquals(
            "extracted text",
            "Hello World",
            visitor.getExtractedText()
        );
    }

    public void testVisitHtmlWithSpecialChars() throws Exception {
        createParser("<BODY>Hello World&nbsp;&nbsp;</BODY>");
        TextExtractingVisitor visitor = new TextExtractingVisitor();
        parser.visitAllNodesWith(visitor);
        assertStringEquals(
            "extracted text",
            "Hello World  ",
            visitor.getExtractedText()
        );
    }

    public void testVisitHtmlWithPreTags() throws Exception {
        createParser(
            "Some text with &nbsp;<pre>this &nbsp; should be preserved</pre>"
        );
        TextExtractingVisitor visitor = new TextExtractingVisitor();
        parser.visitAllNodesWith(visitor);
        assertStringEquals(
            "extracted text",
            "Some text with  this &nbsp; should be preserved",
            visitor.getExtractedText()
        );
    }
}
