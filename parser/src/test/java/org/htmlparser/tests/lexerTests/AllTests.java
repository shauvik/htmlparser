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

package org.htmlparser.tests.lexerTests;

import junit.framework.TestSuite;

import org.htmlparser.tests.ParserTestCase;

public class AllTests extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.lexerTests.AllTests", "AllTests");
    }

    public AllTests (String name)
    {
        super (name);
    }

    public static TestSuite suite ()
    {
        TestSuite suite = new TestSuite ("Lexer Tests");
        suite.addTestSuite (StreamTests.class);
        suite.addTestSuite (SourceTests.class);
        suite.addTestSuite (PageTests.class);
        suite.addTestSuite (PageIndexTests.class);
        suite.addTestSuite (LexerTests.class);
        suite.addTestSuite (AttributeTests.class);
        suite.addTestSuite (TagTests.class);
        return suite;
    }
}
