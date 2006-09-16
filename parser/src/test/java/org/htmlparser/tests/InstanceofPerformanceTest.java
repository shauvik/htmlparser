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

import java.util.Enumeration;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.FormTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.SimpleNodeIterator;

public class InstanceofPerformanceTest {

    public static final String FORM_HTML =
    "<FORM METHOD=\""+FormTag.POST+"\" ACTION=\"do_login.php\" NAME=\"login_form\" onSubmit=\"return CheckData()\">\n"+
        "<TR><TD ALIGN=\"center\">&nbsp;</TD></TR>\n"+
        "<TR><TD ALIGN=\"center\"><FONT face=\"Arial, verdana\" size=2><b>User Name</b></font></TD></TR>\n"+
        "<TR><TD ALIGN=\"center\"><INPUT TYPE=\"text\" NAME=\"name\" SIZE=\"20\"></TD></TR>\n"+
        "<TR><TD ALIGN=\"center\"><FONT face=\"Arial, verdana\" size=2><b>Password</b></font></TD></TR>\n"+
        "<TR><TD ALIGN=\"center\"><INPUT TYPE=\"password\" NAME=\"passwd\" SIZE=\"20\"></TD></TR>\n"+
        "<TR><TD ALIGN=\"center\">&nbsp;</TD></TR>\n"+
        "<TR><TD ALIGN=\"center\"><INPUT TYPE=\"submit\" NAME=\"submit\" VALUE=\"Login\"></TD></TR>\n"+
        "<TR><TD ALIGN=\"center\">&nbsp;</TD></TR>\n"+
        "<TEXTAREA name=\"Description\" rows=\"15\" cols=\"55\" wrap=\"virtual\" class=\"composef\" tabindex=\"5\">Contents of TextArea</TEXTAREA>\n"+
//      "<TEXTAREA name=\"AnotherDescription\" rows=\"15\" cols=\"55\" wrap=\"virtual\" class=\"composef\" tabindex=\"5\">\n"+
        "<INPUT TYPE=\"hidden\" NAME=\"password\" SIZE=\"20\">\n"+
        "<INPUT TYPE=\"submit\">\n"+
        "</FORM>";

    FormTag formTag;
    Vector formChildren;
    public void setUp() throws Exception {
        Parser parser =
            Parser.createParser(
                FORM_HTML,
                null
            );
        NodeIterator e = parser.elements();
        Node node = e.nextNode();
        formTag = (FormTag)node;
        formChildren = new Vector();
        for (SimpleNodeIterator se = formTag.children();se.hasMoreNodes();) {
            formChildren.addElement(se.nextNode());
        }
    }

    public void doInstanceofTest(long [] time,int index, long numTimes) {
        System.out.println("doInstanceofTest("+index+")");
        long start = System.currentTimeMillis();
        for (long i=0;i<numTimes;i++) {
            for (Enumeration e = formChildren.elements();e.hasMoreElements();) {
                e.nextElement();
            }
        }
        long end = System.currentTimeMillis();
        time[index] = end-start;
    }

    public void doGetTypeTest(long [] time,int index, long numTimes) {
        System.out.println("doGetTypeTest("+index+")");
        long start = System.currentTimeMillis();
        for (long i=0;i<numTimes;i++) {
            for (SimpleNodeIterator e = formTag.children();e.hasMoreNodes();) {
                e.nextNode();
            }
        }
        long end = System.currentTimeMillis();
        time[index] = end-start;
    }

    public void perform() {
        int numTimes = 30;
        long time1[] = new long[numTimes],
        time2[] = new long[numTimes];

        for (int i=0;i<numTimes;i++)
            doInstanceofTest(time1,i,i*10000);

        for (int i=0;i<numTimes;i++)
            doGetTypeTest(time2,i,i*10000);

        print(time1,time2);
    }

    public void print(long [] time1, long [] time2) {
        for (int i=0;i<time1.length;i++) {
            System.out.println(i*1000000+":"+","+time1[i]+"  "+time2[i]);
        }
    }
    public static void main(String [] args) throws Exception {
        InstanceofPerformanceTest test =
            new InstanceofPerformanceTest();
        test.setUp();
        test.perform();
    }
}
