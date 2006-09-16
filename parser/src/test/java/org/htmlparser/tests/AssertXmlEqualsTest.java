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

package org.htmlparser.tests;

import junit.framework.TestSuite;


public class AssertXmlEqualsTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.AssertXmlEqualsTest", "AssertXmlEqualsTest");
    }

    public AssertXmlEqualsTest(String name) {
        super(name);
    }

    public void testNestedTagWithText() throws Exception {
        assertXmlEquals("nested with text","<hello>   <hi>My name is Nothing</hi></hello>","<hello><hi>My name is Nothing</hi>  </hello>");
    }

    public void testThreeTagsDifferent() throws Exception {
        assertXmlEquals("two tags different","<someTag></someTag><someOtherTag>","<someTag/><someOtherTag>");
    }

    public void testOneTag() throws Exception {
        assertXmlEquals("one tag","<someTag>","<someTag>");
    }

    public void testTwoTags() throws Exception {
        assertXmlEquals("two tags","<someTag></someTag>","<someTag></someTag>");
    }

    public void testTwoTagsDifferent() throws Exception {
        assertXmlEquals("two tags different","<someTag></someTag>","<someTag/>");
    }

    public void testTwoTagsDifferent2() throws Exception {
        assertXmlEquals("two tags different","<someTag/>","<someTag></someTag>");
    }

    public void testTwoTagsWithSameAttributes() throws Exception {
        assertXmlEquals("attributes","<tag name=\"John\" age=\"22\" sex=\"M\"/>","<tag sex=\"M\" name=\"John\" age=\"22\"/>");
    }

    public void testTagWithText() throws Exception {
        assertXmlEquals("text","<hello>   My name is Nothing</hello>","<hello>My name is Nothing  </hello>");
    }

    public void testStringWithLineBreaks() throws Exception {
        assertXmlEquals("string with line breaks","testing & refactoring","testing &\nrefactoring");
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite("XML Tests");
        suite.addTestSuite(AssertXmlEqualsTest.class);
        return (suite);
    }
}
