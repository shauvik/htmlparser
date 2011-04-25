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

import org.htmlparser.beans.StringBean;
import org.htmlparser.tests.ParserTestCase;

/**
 * Test case for bug #1161137 Non English Character web page.
 * Submitted by Michael (tilosdcal@users.sourceforge.net)
 */
public class NonEnglishTest extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.utilTests.NonEnglishTest", "NonEnglishTest");
    }

    public NonEnglishTest (String name)
    {
        super(name);
    }

    public void testNonEnglishCharacters () 
    {
        StringBean sb;
        
        sb = new StringBean ();
        sb.setURL ("http://www.kobe-np.co.jp/");
        sb.getStrings ();
        sb.setURL ("http://book.asahi.com/"); // this used to throw an exception
        sb.getStrings ();
    }
}
