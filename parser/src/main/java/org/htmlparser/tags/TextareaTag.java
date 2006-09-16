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

package org.htmlparser.tags;

/**
 * A text area tag within a form.
 */
public class TextareaTag extends CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"TEXTAREA"};

    /**
     * The set of tag names that indicate the end of this tag.
     */
    private static final String[] mEnders = new String[] {"INPUT", "TEXTAREA", "SELECT", "OPTION"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"FORM", "BODY", "HTML"};

    /**
     * Create a new text area tag.
     */
    public TextareaTag ()
    {
    }

    /**
     * Return the set of names handled by this tag.
     * @return The names to be matched that create tags of this type.
     */
    public String[] getIds ()
    {
        return (mIds);
    }

    /**
     * Return the set of tag names that cause this tag to finish.
     * @return The names of following tags that stop further scanning.
     */
    public String[] getEnders ()
    {
        return (mEnders);
    }

    /**
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }

    /**
     * Return the plain text contents from this text area.
     * @return The text of the children of this <code>TEXTAREA</code> tag.
     */
    public String getValue() {
        return toPlainTextString();
    }
}
