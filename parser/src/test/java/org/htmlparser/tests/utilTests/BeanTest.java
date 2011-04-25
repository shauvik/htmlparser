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

package org.htmlparser.tests.utilTests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.beans.LinkBean;
import org.htmlparser.beans.StringBean;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tests.*;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

public class BeanTest extends ParserTestCase
{
    static
    {
        System.setProperty ("org.htmlparser.tests.utilTests.BeanTest", "BeanTest");
    }

    public BeanTest (String name)
    {
        super (name);
    }

    protected byte[] pickle (Object object)
        throws
            IOException
    {
        ByteArrayOutputStream bos;
        ObjectOutputStream oos;
        byte[] ret;

        bos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (bos);
        oos.writeObject (object);
        oos.close ();
        ret = bos.toByteArray ();

        return (ret);
    }

    protected Object unpickle (byte[] data)
        throws
            IOException,
            ClassNotFoundException
    {
        ByteArrayInputStream bis;
        ObjectInputStream ois;
        Object ret;

        bis = new ByteArrayInputStream (data);
        ois = new ObjectInputStream (bis);
        ret = ois.readObject ();
        ois.close ();

        return (ret);
    }

    /**
     * Makes sure that the bean returns text when passed the html.
     */
    protected void check (StringBean bean, String html, String text)
    {
        String path;
        File file;
        PrintWriter out;
        String string;

        path = System.getProperty ("user.dir");
        if (!path.endsWith (File.separator))
            path += File.separator;
        file = new File (path + "delete_me.html");
        try
        {
            out = new PrintWriter (new FileWriter (file));
            out.print (html);
            out.close ();
            bean.setURL (file.getAbsolutePath ());
            string = bean.getStrings ();
        }
        catch (Exception e)
        {
            fail (e.toString ());
            string = null; // never reached
        }
        finally
        {
            file.delete ();
        }
        assertStringEquals ("stringbean text differs", text, string);
    }

    public void testZeroArgPageConstructor ()
        throws
            IOException,
            ClassNotFoundException
    {
        Page page;
        byte[] data;

        page = new Page ();
        data = pickle (page);
        page = (Page)unpickle (data);
    }

    public void testZeroArgLexerConstructor ()
        throws
            IOException,
            ClassNotFoundException
    {
        Lexer lexer;
        byte[] data;

        lexer = new Lexer ();
        data = pickle (lexer);
        lexer = (Lexer)unpickle (data);
    }

    public void testZeroArgParserConstructor ()
        throws
            IOException,
            ClassNotFoundException
{
        Parser parser;
        byte[] data;

        parser = new Parser ();
        data = pickle (parser);
        parser = (Parser)unpickle (data);
    }

    public void testSerializable ()
        throws
            IOException,
            ClassNotFoundException,
            ParserException
    {
        Parser parser;
        Vector<Node> vector;
        NodeIterator enumeration;
        byte[] data;

        parser = new Parser ("http://htmlparser.sourceforge.net/test/example.html");
        enumeration = parser.elements ();
        vector = new Vector<Node> (50);
        while (enumeration.hasMoreNodes ())
            vector.addElement (enumeration.nextNode ());

        data = pickle (parser);
        parser = (Parser)unpickle (data);

        enumeration = parser.elements ();
        while (enumeration.hasMoreNodes ())
            assertEquals (
                "Nodes before and after serialization differ",
                (vector.remove (0)).toHtml (),
                enumeration.nextNode ().toHtml ());
    }

    public void testSerializableScanners ()
        throws
            IOException,
            ClassNotFoundException,
            ParserException
    {
        Parser parser;
        Vector<Node> vector;
        NodeIterator enumeration;
        byte[] data;

        parser = new Parser ("http://htmlparser.sourceforge.net/test/example.html");
        enumeration = parser.elements ();
        vector = new Vector<Node> (50);
        while (enumeration.hasMoreNodes ())
            vector.addElement (enumeration.nextNode ());

        data = pickle (parser);
        parser = (Parser)unpickle (data);

        enumeration = parser.elements ();
        while (enumeration.hasMoreNodes ())
            assertEquals (
                "Nodes before and after serialization differ",
                (vector.remove (0)).toHtml (),
                enumeration.nextNode ().toHtml ());
    }

    public void testSerializableStringBean ()
        throws
            IOException,
            ClassNotFoundException
    {
        StringBean sb;
        String text;
        byte[] data;

        sb = new StringBean ();
        sb.setURL ("http://htmlparser.sourceforge.net/test/example.html");
        text = sb.getStrings ();

        data = pickle (sb);
        sb = (StringBean)unpickle (data);

        assertEquals (
            "Strings before and after serialization differ",
            text,
            sb.getStrings ());
    }

    public void testSerializableLinkBean ()
        throws
            IOException,
            ClassNotFoundException
    {
        LinkBean lb;
        URL[] links;
        byte[] data;
        URL[] links2;

        lb = new LinkBean ();
        lb.setURL ("http://htmlparser.sourceforge.net/test/example.html");
        links = lb.getLinks ();

        data = pickle (lb);
        lb = (LinkBean)unpickle (data);

        links2 = lb.getLinks ();
        assertEquals ("Number of links after serialization differs", links.length, links2.length);
        for (int i = 0; i < links.length; i++)
        {
            assertEquals (
                "Links before and after serialization differ",
                links[i],
                links2[i]);
        }
    }

    public void testStringBeanListener ()
    {
        final StringBean sb;
        final Boolean hit[] = new Boolean[1];

        sb = new StringBean ();
        hit[0] = Boolean.FALSE;
        sb.addPropertyChangeListener (
            new PropertyChangeListener ()
            {
                public void propertyChange (PropertyChangeEvent event)
                {
                    if (event.getSource ().equals (sb))
                        if (event.getPropertyName ().equals (StringBean.PROP_STRINGS_PROPERTY))
                            hit[0] = Boolean.TRUE;
                }
            });

        hit[0] = Boolean.FALSE;
        sb.setURL ("http://htmlparser.sourceforge.net/test/example.html");
        assertTrue (
            "Strings property change not fired for URL change",
            hit[0].booleanValue ());

        hit[0] = Boolean.FALSE;
        sb.setLinks (true);
        assertTrue (
            "Strings property change not fired for links change",
            hit[0].booleanValue ());
    }

    public void testLinkBeanListener ()
    {
        final LinkBean lb;
        final Boolean hit[] = new Boolean[1];

        lb = new LinkBean ();
        hit[0] = Boolean.FALSE;
        lb.addPropertyChangeListener (
            new PropertyChangeListener ()
            {
                public void propertyChange (PropertyChangeEvent event)
                {
                    if (event.getSource ().equals (lb))
                        if (event.getPropertyName ().equals (LinkBean.PROP_LINKS_PROPERTY))
                            hit[0] = Boolean.TRUE;
                }
            });

        hit[0] = Boolean.FALSE;
        lb.setURL ("http://htmlparser.sourceforge.net/test/example.html");
        assertTrue (
            "Links property change not fired for URL change",
            hit[0].booleanValue ());
    }

    /**
     * Test no text returns empty string.
     */
    public void testCollapsed1 ()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (false);
        check (sb, "<html><head></head><body></body></html>", "");
        check (sb, "<html><head></head><body> </body></html>", " ");
        check (sb, "<html><head></head><body>\t</body></html>", "\t");
        sb.setCollapse (true);
        check (sb, "<html><head></head><body></body></html>", "");
        check (sb, "<html><head></head><body> </body></html>", "");
        check (sb, "<html><head></head><body>\t</body></html>", "");
    }

    /**
     * Test multiple whitespace returns empty string.
     */
    public void testCollapsed2 ()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (false);
        check (sb, "<html><head></head><body>  </body></html>", "  ");
        check (sb, "<html><head></head><body>\t\t</body></html>", "\t\t");
        check (sb, "<html><head></head><body> \t\t</body></html>", " \t\t");
        check (sb, "<html><head></head><body>\t \t</body></html>", "\t \t");
        check (sb, "<html><head></head><body>\t\t </body></html>", "\t\t ");
        sb.setCollapse (true);
        check (sb, "<html><head></head><body>  </body></html>", "");
        check (sb, "<html><head></head><body>\t\t</body></html>", "");
        check (sb, "<html><head></head><body> \t\t</body></html>", "");
        check (sb, "<html><head></head><body>\t \t</body></html>", "");
        check (sb, "<html><head></head><body>\t\t </body></html>", "");
    }

    /**
     * Test text preceded or followed by whitespace returns just text.
     */
    public void testCollapsed3 ()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (false);
        check (sb, "<html><head></head><body>x  </body></html>", "x  ");
        check (sb, "<html><head></head><body>x\t\t</body></html>", "x\t\t");
        check (sb, "<html><head></head><body>x \t\t</body></html>", "x \t\t");
        check (sb, "<html><head></head><body>x\t \t</body></html>", "x\t \t");
        check (sb, "<html><head></head><body>x\t\t </body></html>", "x\t\t ");
        sb.setCollapse (true);
        check (sb, "<html><head></head><body>x  </body></html>", "x");
        check (sb, "<html><head></head><body>x\t\t</body></html>", "x");
        check (sb, "<html><head></head><body>x \t\t</body></html>", "x");
        check (sb, "<html><head></head><body>x\t \t</body></html>", "x");
        check (sb, "<html><head></head><body>x\t\t </body></html>", "x");
        check (sb, "<html><head></head><body>  x</body></html>", "x");
        check (sb, "<html><head></head><body>\t\tx</body></html>", "x");
        check (sb, "<html><head></head><body> \t\tx</body></html>", "x");
        check (sb, "<html><head></head><body>\t \tx</body></html>", "x");
        check (sb, "<html><head></head><body>\t\t x</body></html>", "x");
    }

    /**
     * Test text including a "pre" tag
     */
    public void testOutputWithPreTags() {
        StringBean sb;
        sb = new StringBean ();
        String sampleCode = "public class Product {}";
        check (sb, "<body><pre>"+sampleCode+"</pre></body>", sampleCode);
    }

    /**
     * Test text including a "script" tag
     */
    public void testOutputWithScriptTags() {
        StringBean sb;
        sb = new StringBean ();

        String sampleScript =
          "<script language=\"javascript\">\r\n"
        + "if(navigator.appName.indexOf(\"Netscape\") != -1)\r\n"
        + " document.write ('xxx');\r\n"
        + "else\r\n"
        + " document.write ('yyy');\r\n"
        + "</script>\r\n";

        check (sb, "<body>"+sampleScript+"</body>", "");
    }

    /*
     * Test output with pre and any tag.
     */
    public void testOutputWithPreAndAnyTag()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (false);
        check (sb, "<html><head></head><body><pre><hello></pre></body></html>", "");
    }

    /*
     * Test output with pre and any tag and text.
     */
    public void testOutputWithPreAndAnyTagPlusText()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (false);
        check (sb, "<html><head></head><body><pre><hello>dogfood</hello></pre></body></html>", "dogfood");
    }

    /*
     * Test output with pre and any tag and text.
     */
    public void testOutputWithPreAndAnyTagPlusTextWithWhitespace()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (true);
        check (sb, "<html><head></head><body><pre><hello>dog  food</hello></pre></body></html>", "dog  food");
    }

    /*
     * Test output without pre and any tag and text.
     */
    public void testOutputWithoutPreAndAnyTagPlusTextWithWhitespace()
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (true);
        check (sb, "<html><head></head><body><hello>dog  food</hello></body></html>", "dog food");
    }

    /**
     * Test output with pre and script tags
     */
    public void xtestOutputWithPreAndScriptTags ()
    {
        StringBean sb;
        sb = new StringBean ();

        String sampleScript =
          "<script language=\"javascript\">\r\n"
        + "if(navigator.appName.indexOf(\"Netscape\") != -1)\r\n"
        + " document.write ('xxx');\r\n"
        + "else\r\n"
        + " document.write ('yyy');\r\n"
        + "</script>\r\n";

        check (sb, "<body><pre>"+sampleScript+"</pre></body>", sampleScript);
    }

    /**
     * Test output with non-breaking tag within text.
     */
    public void testTagWhitespace ()
    {
        StringBean sb;
        sb = new StringBean ();

        String pre = "AAAAA BBBBB AAA";
        String mid = "AA";
        String post = " BBBBB";
        String html =
          "<HTML>\r\n"
        + "<body>\r\n"
        + "<p>" + pre + "<font color='red'>" + mid + "</font>" + post + "</p>\r\n"
        + "</body>\r\n"
        + "</HTML>\r\n";

        check (sb, html, pre + mid + post);
    }

    /*
     * Bug #1574684 text extracted merges words in some cases
     * Check that a newline is added after a heading end tag.
     */
    public void testHeadingBreak () throws Exception
    {
        String html = "<HTML><HEAD><TITLE>Hebrews Chapter 1 - American Standard Version</TITLE><link rel=stylesheet href=style1.css type=text/css><META NAME='keywords' CONTENT='American Standard Version, Hebrews 1'><META NAME='description' CONTENT='Hebrews Chapter 1.'><LINK REL='SHORTCUT ICON' href='cross.ico'><META HTTP-EQUIV=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"></HEAD><BODY BACKGROUND='Image1.jpg'><FORM name=MyForm><H3 ALIGN=CENTER>American Standard Version</H3><A HREF=B57C001.htm>Philemon 1</A><H2 ALIGN=CENTER>The Epistle to the Hebrews</H2><P><A HREF='index.htm'>Return to Index</A></P><CENTER><TABLE BORDER=0 CELLPADDING=5 WIDTH=90%><TR><TD></TD><TD><CENTER><H1>Chapter 1</H1></CENTER></TD></TR><TR><TD VALIGN=TOP><A NAME='V1'><H4>1</H4></TD><TD><P>God, having of old time spoken unto the fathers in the prophets by divers portions and in divers manners,<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V2'><H4>2</H4></TD><TD><P>hath at the end of these days spoken unto us in [his] Son, whom he appointed heir of all things, through whom also he made the worlds;<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V3'><H4>3</H4></TD><TD><P>who being the effulgence of his glory, and the very image of his substance, and upholding all things by the word of his power, when he had made purification of sins, sat down on the right hand of the Majesty on high;<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V4'><H4>4</H4></TD><TD><P>having become by so much better than the angels, as he hath inherited a more excellent name than they.<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V5'><H4>5</H4></TD><TD><P>For unto which of the angels said he at any time, Thou art my Son, This day have I begotten thee?     and again, I will be to him a Father, And he shall be to me a Son?<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V6'><H4>6</H4></TD><TD><P>And when he again bringeth in the firstborn into the world he saith, And let all the angels of God worship him.<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V7'><H4>7</H4></TD><TD><P>And of the angels he saith, Who maketh his angels winds, And his ministers a flame a fire:<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V8'><H4>8</H4></TD><TD><P>but of the Son [he saith,] Thy throne, O God, is for ever and ever; And the sceptre of uprightness is the sceptre of thy kingdom.<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V9'><H4>9</H4></TD><TD><P>Thou hast loved righteousness, and hated iniquity; Therefore God, thy God, hath anointed thee With the oil of gladness above thy fellows.<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V10'><H4>10</H4></TD><TD><P>And, Thou, Lord, in the beginning didst lay the foundation of the       earth, And the heavens are the works of thy hands:<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V11'><H4>11</H4></TD><TD><P>They shall perish; but thou continuest: And they all shall wax old as doth a garment;<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V12'><H4>12</H4></TD><TD><P>And as a mantle shalt thou roll them up, As a garment, and they shall be changed: But thou art the same, And thy years shall not fail.<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V13'><H4>13</H4></TD><TD><P>But of which of the angels hath he said at any time, Sit thou on my right hand, Till I make thine enemies the footstool of thy feet?<P></TD></TR><TR><TD VALIGN=TOP><A NAME='V14'><H4>14</H4></TD><TD><P>Are they not all ministering spirits, sent forth to do service for the sake of them that shall inherit salvation?<P></TD></TR></TABLE><!--Created by John M. Hurt, PO Box 31, Elmwood, TN USA jhurt@johnhurt.com--><P></P><A HREF=B58C002.htm>Hebrews 2</A><P><P ALIGN='CENTER'><IMG SRC='line6.gif' WIDTH=300 HEIGHT=12></P><P>&nbsp;<P>&nbsp;<P>&nbsp;<P><A HREF='about.htm'><FONT FACE=ARIAL size=2>Public Domain</A> Software <A HREF='http://www.htmlbible.com'><FONT FACE=ARIAL size=2>by</A> <A HREF='http://www.johnhurt.com'><FONT FACE=ARIAL size=2>johnhurt.com</A></FONT></CENTER></BODY></HTML>";
        Parser parser;
        StringBean sb;
        String s;
        int index;
        String separator;
        String newline;
        
        parser = new Parser (html);
        sb = new StringBean ();
        parser.visitAllNodesWith (sb);
        s = sb.getStrings ();
        index = s.indexOf ("Philemon 1");
        assertTrue (
            "text not right",
            -1 != index);
        separator = System.getProperty ("line.separator");
        newline = s.substring (index - separator.length (), index);
        assertTrue (
            "no newline after heading",
            newline.equals (separator));

    }
}

