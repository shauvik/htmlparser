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

package org.htmlparser.tests.tagTests;

import junit.framework.TestSuite;

import org.htmlparser.tests.ParserTestCase;

public class AllTests extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.AllTests", "AllTests");
    }

    public AllTests(String name) {
        super(name);
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite("Tag Tests");
        suite.addTestSuite(AppletTagTest.class);
        suite.addTestSuite(BaseHrefTagTest.class);
        suite.addTestSuite(BodyTagTest.class);
        suite.addTestSuite(BulletListTagTest.class);
        suite.addTestSuite(BulletTagTest.class);
        suite.addTestSuite(CompositeTagTest.class);
        suite.addTestSuite(DivTagTest.class);
        suite.addTestSuite(DoctypeTagTest.class);
        suite.addTestSuite(EndTagTest.class);
        suite.addTestSuite(FormTagTest.class);
        suite.addTestSuite(FrameSetTagTest.class);
        suite.addTestSuite(FrameTagTest.class);
        suite.addTestSuite(HeadTagTest.class);
        suite.addTestSuite(HtmlTagTest.class);
        suite.addTestSuite(ImageTagTest.class);
        suite.addTestSuite(InputTagTest.class);
        suite.addTestSuite(JspTagTest.class);
        suite.addTestSuite(LabelTagTest.class);
        suite.addTestSuite(LinkTagTest.class);
        suite.addTestSuite(MetaTagTest.class);
        suite.addTestSuite(ObjectCollectionTest.class);
        suite.addTestSuite(OptionTagTest.class);
        suite.addTestSuite(ScriptTagTest.class);
        suite.addTestSuite(SelectTagTest.class);
        suite.addTestSuite(SpanTagTest.class);
        suite.addTestSuite(StyleTagTest.class);
        suite.addTestSuite(TableTagTest.class);
        suite.addTestSuite(TagTest.class);
        suite.addTestSuite(TextareaTagTest.class);
        suite.addTestSuite(TitleTagTest.class);
        return suite;
    }
}
