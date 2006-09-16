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

package org.htmlparser.tests.utilTests;


import junit.framework.TestSuite;

import org.htmlparser.tests.ParserTestCase;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2001 6:07:04 PM)
 */
public class AllTests extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.utilTests.AllTests", "AllTests");
    }

    /**
     * AllTests constructor comment.
     * @param name java.lang.String
     */
    public AllTests(String name) {
        super(name);
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/17/2001 6:07:15 PM)
     * @return junit.framework.TestSuite
     */
    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite("Utility Tests");

        suite.addTestSuite(BeanTest.class);
        suite.addTestSuite(HTMLParserUtilsTest.class);
        suite.addTestSuite(NodeListTest.class);
        suite.addTestSuite(NonEnglishTest.class);
        suite.addTestSuite(SortTest.class);

        return suite;
    }
}
