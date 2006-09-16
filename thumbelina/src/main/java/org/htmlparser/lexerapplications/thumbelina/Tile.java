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

package org.htmlparser.lexerapplications.thumbelina;

import java.awt.Rectangle;

/**
 * Specifies the interface for Tile objects handled by the TileSet class.
 * Basically a Rectangle with auxillary attributes.
 */
public interface Tile extends Cloneable
{
    /**
     * Get the rectangular region for this tile.
     * @return A rectangle boundary.
     */
    Rectangle getBounds ();

    /**
     * Set the rectangular region for this tile.
     * Crops the tile to the size provided.
     * @param rectangle The new boundary.
     */
    void setBounds (Rectangle rectangle);

    /*
     * Returns true if this tile is valid.
     * @return <code>true</code> when the tile has not been marked invalid.
     */
    boolean getValid ();

    /**
     * Sets the validity of the tile.
     * @param valid If <code>true</code> the tile is marked valid,
     * false otherwise.
     */
    void setValid (boolean valid);
    
    /**
     * Return the distinguishing identity for this tile.
     * @return An object suitable for use in hashing or testing for equality.
     */
    Object getIdentity ();

    /**
     * Set the distinguishing identity for this tile.
     * This will usually have semantic meaning for the tile, so it should
     * be of the type expected by the implementing class.
     * @param object An object defining this tile's identity.
     */
    void setIdentity (Object object);

    /**
     * Reset the tile to it's uncropped size.
     */
    void reset ();

    /**
     * Clone this object.
     * Exposes java.lang.Object clone() as a public method.
     * @return A clone of this object.
     */
    Object clone ();
}
