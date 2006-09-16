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

import org.htmlparser.tests.ParserTestCase;

public class AllTests extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.AllTests", "AllTests");
    }

    public AllTests(String name) {
        super(name);
    }

    public static TestSuite suite()
    {
        TestSuite suite;
        TestSuite sub;
        
        suite = new TestSuite ("HTMLParser Tests");
        sub = new TestSuite ("Basic Tests");
        sub.addTestSuite (ParserTest.class);
        sub.addTestSuite (AssertXmlEqualsTest.class);
        sub.addTestSuite (FunctionalTests.class);
        sub.addTestSuite (LineNumberAssignedByNodeReaderTest.class);
        suite.addTest (sub);
        suite.addTest (org.htmlparser.tests.lexerTests.AllTests.suite ());
        suite.addTest (org.htmlparser.tests.scannersTests.AllTests.suite ());
        suite.addTest (org.htmlparser.tests.utilTests.AllTests.suite ());
        suite.addTest (org.htmlparser.tests.tagTests.AllTests.suite ());
        suite.addTest (org.htmlparser.tests.visitorsTests.AllTests.suite ());
        suite.addTest (org.htmlparser.tests.parserHelperTests.AllTests.suite ());
        suite.addTestSuite (org.htmlparser.tests.filterTests.FilterTest.class);

        return (suite);
    }
}

