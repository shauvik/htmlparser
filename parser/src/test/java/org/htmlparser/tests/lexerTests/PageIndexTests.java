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

import org.htmlparser.lexer.PageIndex;
import org.htmlparser.tests.ParserTestCase;

public class PageIndexTests extends ParserTestCase
{

    static
    {
        System.setProperty ("org.htmlparser.tests.lexerTests.PageIndexTests", "PageIndexTests");
    }

    /**
     * Test the end-of-line index class.
     */
    public PageIndexTests (String name)
    {
        super (name);
    }

    public void testAppend1 ()
    {
        PageIndex index;
        int pos;
        int[] list;

        index = new PageIndex (null);

        for (int i = 0; i < 10000; i++)
        {
            pos = index.row (i);
            assertTrue ("append not at end", pos == i);
            assertTrue ("wrong position", pos == index.add (i));
        }

        list = index.get ();
        for (int i = 0; i < 10000; i++)
            assertTrue ("wrong value", list[i] == i);
    }

    public void testAppend2 ()
    {
        PageIndex index;
        int pos;
        int[] list;

        index = new PageIndex (null);

        for (int i = 0; i < 10000; i++)
        {
            pos = index.row (i + 42);
            assertTrue ("append not at end", pos == i);
            assertTrue ("wrong position", pos == index.add (i + 42));
        }

        list = index.get ();
        for (int i = 0; i < 10000; i++)
            assertTrue ("wrong value", list[i] == i + 42);
    }

    public void testAppend3 ()
    {
        PageIndex index;
        int pos;
        int[] list;

        index = new PageIndex (null);

        for (int i = 0; i < 10000; i++)
        {
            pos = index.row (i * 42);
            assertTrue ("append not at end", pos == i);
            assertTrue ("wrong position", pos == index.add (i * 42));
        }

        list = index.get ();
        for (int i = 0; i < 10000; i++)
            assertTrue ("wrong value", list[i] == i * 42);
    }

    public void testInsert ()
    {
        PageIndex index;
        double d;
        int n;
        int pos;
        int[] list;

        index = new PageIndex (null);

        for (int i = 0; i < 10000; i++)
        {
            d = Math.random ();
            d -= 0.5;
            n = (int)(d * 100838);
            pos = index.row (n);

            // test for correct position
            if (0 <= pos - 1)
                assertTrue ("search error less " + pos + " " + index.elementAt (pos - 1) + " " + n, index.elementAt (pos - 1) <= n);
            if (pos + 1 < index.size ())
                assertTrue ("search error greater " + pos + " " + index.elementAt (pos + 1) + " " + n, index.elementAt (pos + 1) > n);
        }

        list = index.get ();
        n = Integer.MIN_VALUE;
        for (int i = 0; i < list.length; i++)
        {
            assertTrue ("wrong order", list[i] > n);
            n = list[i];
        }
    }
}
