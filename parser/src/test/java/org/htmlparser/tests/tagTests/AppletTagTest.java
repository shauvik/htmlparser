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

import java.util.Enumeration;
import java.util.Hashtable;

import org.htmlparser.tags.AppletTag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class AppletTagTest extends ParserTestCase {

    static
    {
        System.setProperty ("org.htmlparser.tests.tagTests.AppletTagTest", "AppletTagTest");
    }

    public AppletTagTest(String name) {
        super(name);
    }

    public void testToHTML() throws ParserException {
        String [][]paramsData = {{"Param1","Value1"},{"Name","Somik"},{"Age","23"}};
        Hashtable paramsMap = new Hashtable();
        String testHTML = new String("<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n");
        for (int i = 0;i<paramsData.length;i++)
        {
            testHTML+="<PARAM NAME=\""+paramsData[i][0]+"\" VALUE=\""+paramsData[i][1]+"\">\n";
            paramsMap.put(paramsData[i][0],paramsData[i][1]);
        }
        testHTML+=
            "</APPLET>\n"+
            "</HTML>";
        createParser(testHTML);
        parseAndAssertNodeCount(3);
        assertTrue("Node should be an applet tag",node[0] instanceof AppletTag);
        // Check the data in the applet tag
        AppletTag appletTag = (AppletTag)node[0];
        String expectedRawString =
        "<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n"+
        "<PARAM NAME=\"Param1\" VALUE=\"Value1\">\n"+
        "<PARAM NAME=\"Name\" VALUE=\"Somik\">\n"+
        "<PARAM NAME=\"Age\" VALUE=\"23\">\n"+
        "</APPLET>";
        assertStringEquals("toHTML()",expectedRawString,appletTag.toHtml());
    }

    public void testScan() throws ParserException
    {
        String [][]paramsData = {{"Param1","Value1"},{"Name","Somik"},{"Age","23"}};
        Hashtable paramsMap = new Hashtable();
        String testHTML = new String("<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n");
        for (int i = 0;i<paramsData.length;i++)
        {
            testHTML+="<PARAM NAME=\""+paramsData[i][0]+"\" VALUE=\""+paramsData[i][1]+"\">\n";
            paramsMap.put(paramsData[i][0],paramsData[i][1]);
        }
        testHTML+=
            "</APPLET></HTML>";
        createParser(testHTML);
        parseAndAssertNodeCount(2);
        assertTrue("Node should be an applet tag",node[0] instanceof AppletTag);
        // Check the data in the applet tag
        AppletTag appletTag = (AppletTag)node[0];
        assertEquals("Class Name","Myclass.class",appletTag.getAppletClass());
        assertEquals("Archive","test.jar",appletTag.getArchive());
        assertEquals("Codebase","www.kizna.com",appletTag.getCodeBase());
        // Check the params data
        int cnt = 0;
        for (Enumeration e = appletTag.getParameterNames();e.hasMoreElements();)
        {
            String paramName = (String)e.nextElement();
            String paramValue = appletTag.getParameter(paramName);
            assertEquals("Param "+cnt+" value",paramsMap.get(paramName),paramValue);
            cnt++;
        }
        assertEquals("Number of params",new Integer(paramsData.length),new Integer(cnt));
    }
    
    public void testChangeCodebase() throws ParserException {
        String [][]paramsData = {{"Param1","Value1"},{"Name","Somik"},{"Age","23"}};
        Hashtable paramsMap = new Hashtable();
        String testHTML = new String("<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n");
        for (int i = 0;i<paramsData.length;i++)
        {
            testHTML+="<PARAM NAME=\""+paramsData[i][0]+"\" VALUE=\""+paramsData[i][1]+"\">\n";
            paramsMap.put(paramsData[i][0],paramsData[i][1]);
        }
        testHTML+=
            "</APPLET>\n"+
            "</HTML>";
        createParser(testHTML);
        parseAndAssertNodeCount(3);
        assertTrue("Node should be an applet tag",node[0] instanceof AppletTag);
        AppletTag appletTag = (AppletTag)node[0];
        appletTag.setCodeBase ("htmlparser.sourceforge.net");
        // Check the data in the applet tag
        String expectedRawString =
        "<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=htmlparser.sourceforge.net>\n"+
        "<PARAM NAME=\"Param1\" VALUE=\"Value1\">\n"+
        "<PARAM NAME=\"Name\" VALUE=\"Somik\">\n"+
        "<PARAM NAME=\"Age\" VALUE=\"23\">\n"+
        "</APPLET>";
        assertStringEquals("toHTML()",expectedRawString,appletTag.toHtml());
    }

    public void testChangeArchive() throws ParserException {
        String [][]paramsData = {{"Param1","Value1"},{"Name","Somik"},{"Age","23"}};
        Hashtable paramsMap = new Hashtable();
        String testHTML = "<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n";
        for (int i = 0;i<paramsData.length;i++)
        {
            testHTML+="<PARAM NAME=\""+paramsData[i][0]+"\" VALUE=\""+paramsData[i][1]+"\">\n";
            paramsMap.put(paramsData[i][0],paramsData[i][1]);
        }
        testHTML +=
            "</APPLET>";
        createParser(testHTML + "\n</HTML>");
        parseAndAssertNodeCount(3);
        assertTrue("Node should be an applet tag",node[0] instanceof AppletTag);
        AppletTag appletTag = (AppletTag)node[0];
        appletTag.setArchive ("htmlparser.jar");
        // Check the data in the applet tag
        testHTML = testHTML.substring (0, testHTML.indexOf ("test.jar"))
            + "htmlparser.jar"
            + testHTML.substring (testHTML.indexOf ("test.jar") + 8);
        assertStringEquals("toHTML()",testHTML,appletTag.toHtml());
    }

    public void testChangeAppletClass() throws ParserException {
        String [][]paramsData = {{"Param1","Value1"},{"Name","Somik"},{"Age","23"}};
        Hashtable paramsMap = new Hashtable();
        String testHTML = new String("<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n");
        for (int i = 0;i<paramsData.length;i++)
        {
            testHTML+="<PARAM NAME=\""+paramsData[i][0]+"\" VALUE=\""+paramsData[i][1]+"\">\n";
            paramsMap.put(paramsData[i][0],paramsData[i][1]);
        }
        testHTML+=
            "</APPLET>";
        createParser(testHTML + "\n</HTML>");
        parseAndAssertNodeCount(3);
        assertTrue("Node should be an applet tag",node[0] instanceof AppletTag);
        AppletTag appletTag = (AppletTag)node[0];
        appletTag.setAppletClass ("MyOtherClass.class");
        // Check the data in the applet tag
        testHTML = testHTML.substring (0, testHTML.indexOf ("Myclass.class"))
            + "MyOtherClass.class"
            + testHTML.substring (testHTML.indexOf ("Myclass.class") + 13);
        assertStringEquals("toHTML()",testHTML,appletTag.toHtml());
    }

    public void testChangeAppletParams() throws ParserException {
        String [][]paramsData = {{"Param1","Value1"},{"Name","Somik"},{"Age","23"}};
        Hashtable paramsMap = new Hashtable();
        String testHTML = new String("<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n");
        for (int i = 0;i<paramsData.length;i++)
        {
            testHTML+="<PARAM NAME=\""+paramsData[i][0]+"\" VALUE=\""+paramsData[i][1]+"\">\n";
            paramsMap.put(paramsData[i][0],paramsData[i][1]);
        }
        testHTML+=
            "</APPLET>\n"+
            "</HTML>";
        createParser(testHTML);
        parseAndAssertNodeCount(3);
        assertTrue("Node should be an applet tag",node[0] instanceof AppletTag);
        AppletTag appletTag = (AppletTag)node[0];
        paramsMap = new Hashtable();
        String [][] newparamsData = {{"First","One"},{"Second","Two"},{"Third","3"}};
        for (int i = 0;i<paramsData.length;i++)
        {
            paramsMap.put(newparamsData[i][0],newparamsData[i][1]);
        }
        appletTag.setAppletParams (paramsMap);
        // Check the data in the applet tag
        String expectedRawString =
        "<APPLET CODE=Myclass.class ARCHIVE=test.jar CODEBASE=www.kizna.com>\n"+
        "<PARAM VALUE=\"Two\" NAME=\"Second\">"+ // note these are out of orer because of the hashtable
        "<PARAM VALUE=\"One\" NAME=\"First\">"+
        "<PARAM VALUE=\"3\" NAME=\"Third\">"+
        "</APPLET>";
        String actual = appletTag.toHtml();
        assertStringEquals("toHTML()",expectedRawString,actual);
    }
}
