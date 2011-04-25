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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.border.BevelBorder;

/**
 * Hold and display a group of pictures.
 * @author  derrick
 */
public class PicturePanel
    extends
        JPanel
    implements
        MouseListener,
        Scrollable,
        ComponentListener,
        HierarchyListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class Watcher implements ImageObserver
    {
        Component mTarget;
        Image mImage;
        int mX;
        int mY;
        int m_x;
        int m_y;
        public boolean drawn;

        public Watcher (Component component, Image image,
            int x, int y, int _x, int _y)
        {
            mTarget = component;
            mImage = image;
            mX = x;
            mY = y;
            m_x = _x;
            m_y = _y;
            drawn = false;
        }

        public boolean imageUpdate (Image image, int infoflags, int x, int y, int width, int height)
        {
            int w;
            int h;
            boolean ret;

            if (0 != (infoflags & ImageObserver.ALLBITS))
            {
                Graphics graphics = mTarget.getGraphics ();
                if (null != graphics)
                {
                    w = image.getWidth (null);
                    h = image.getHeight (null);
                    graphics.drawImage (image,
                        mX, mY, mX + w, mY + h,
                        m_x, m_y, w, h,
                        null);
                    drawn = true;
                }
                ret = false;
            }
            else
                ret = true;

            return (ret);
        }
    }

    /**
     * Scrolling unit increment (both directions).
     */
    protected static final int UNIT_INCREMENT = 10;

    /**
     * Scrolling block increment (both directions).
     */
    protected static final int BLOCK_INCREMENT = 100;

    /**
     * The thumbelina object in use.
     */
    protected Thumbelina mThumbelina;

    /**
     * The display mosaic.
     */
    protected TileSet mMosaic;

    /**
     * The preferred size of this component.
     * <code>null</code> initially, caches the results of
     * <code>calculatePreferredSize ()</code>.
     */
    protected Dimension mPreferredSize;

    /**
     * Creates a new instance of PicturePanel
     * @param thumbelina The <code>Thumeblina</code> this panel is associated
     * with.
     */
    public PicturePanel (final Thumbelina thumbelina)
    {
        mThumbelina = thumbelina;
        mMosaic = new TileSet ();
        mPreferredSize = null;
        setDoubleBuffered (false);
        setBorder (new BevelBorder (BevelBorder.LOWERED));
        addMouseListener (this);
        addHierarchyListener (this);
    }

    /**
     * Clears the panel, discarding any existing images.
     */
    public void reset ()
    {
        mMosaic = new TileSet ();
        repaint ();
    }

    /**
     * Move the given picture to the top of the Z order.
     * Adds it, even it if it doesn't exist.
     * Also puts the URL in the url text of the status bar.
     * @param tile The tile being brought forward.
     */
    public void bringToTop (final Tile tile)
    {
        Rectangle rectangle;

        mMosaic.bringToTop (tile);
        rectangle = tile.getBounds ();
        repaint (rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        mThumbelina.mUrlText.setText (tile.getIdentity ().toString ());
    }

    /**
     * Find a picture with the given URL in the panel.
     * This should really only be used to discover if the picture is still
     * visible. There could be more than one picture with the given URL
     * because it may be partially obscured by another picture, in which
     * case the pieces are each given their own picture object, but all
     * point at the same <code>URL</code> and <code>Image</code>.
     * @param url The url to locate.
     * @return The first picture encountered in the panel,
     * or null if the picture was not found.
     */
    public Picture find (final String url)
    {
        Iterator<Tile> iterator;
        Picture picture;
        Picture ret;

        ret = null;
        iterator = mMosaic.getTiles ();
        while ((null == ret) && iterator.hasNext ())
        {
            picture = (Picture)iterator.next ();
            if (url.equals (picture.getIdentity ().toString ()))
                ret = picture;
        }

        return (ret);
    }

    /**
     * Draw an image on screen.
     * @param picture The picture to draw.
     * @param add If <code>true</code>, the picture is added to the history.
     */
    protected void draw (final Picture picture, final boolean add)
    {
        Component parent;
        boolean dolayout;
        Dimension before;
        Dimension after;

        parent = getParent ();
        dolayout = false;
        synchronized (mMosaic)
        {
            if (parent instanceof JViewport)
            {
                before = getPreferredSize ();
                mMosaic.add (picture);
                after = calculatePreferredSize ();
                if (after.width > before.width)
                    dolayout = true;
                else
                    after.width = before.width;
                if (after.height > before.height)
                    dolayout = true;
                else
                    after.height = before.height;
                if (dolayout)
                    mPreferredSize = after;
            }
            else
                mMosaic.add (picture);
        }
        if (dolayout)
        {
            revalidate ();
            invalidate ();
            validate ();
        }
        //System.out.print ("" + picture.x + "," + picture.y + "," + picture.width + "," + picture.height);
        repaint (picture.x, picture.y, picture.width, picture.height);
        if (add)
            mThumbelina.addHistory (picture.getIdentity ().toString ());
    }

    /**
     * Updates this component.
     * @param graphics The graphics context in which to update the component.
     */
    public void update (final Graphics graphics)
    {
        paint (graphics);
    }

    /**
     * Adjust the graphics clip region to account for insets.
     * @param graphics The graphics object to set the clip region for.
     **/
    public void adjustClipForInsets (final Graphics graphics)
    {
        Dimension dim;
        Insets insets;
        Rectangle clip;

        dim = getSize ();
        insets = getInsets ();
        clip = graphics.getClipBounds ();
        if (clip.x < insets.left)
            clip.x = insets.left;
        if (clip.y < insets.top)
            clip.y = insets.top;
        if (clip.x + clip.width > dim.width - insets.right)
            clip.width = dim.width - insets.right - clip.x;
        if (clip.y + clip.height > dim.height - insets.bottom)
            clip.height = dim.height - insets.bottom - clip.y;
        graphics.setClip (clip.x, clip.y, clip.width, clip.height);
    }

    /**
     * Paints this component.
     * Runs through the list of tiles and for every one that intersects
     * the clip region performs a <code>drawImage()</code>.
     * @param graphics The graphics context used to paint with.
     */
    public void paint (final Graphics graphics)
    {
        Rectangle clip;
        Iterator<Tile> iterator;
        HashSet<Image> set; // just so we don't draw things twice
        Picture picture;
        Rectangle rectangle;
        Image image;
        Point origin;
        int width;
        int height;

        //adjustClipForInsets (graphics);
        clip = graphics.getClipBounds ();
        synchronized (mMosaic)
        {
            //Rectangle s;
            //s = graphics.getClipBounds ();
            //System.out.print ("" + s.x + "," + s.y + "," + s.width + "," + s.height + " ");
            super.paint (graphics);
            if (0 != mMosaic.getTileCount ())
            {
                super.paint (graphics);
                iterator = mMosaic.getTiles ();
                set = new HashSet<Image> ();
                while (iterator.hasNext ())
                {
                    picture = (Picture)iterator.next ();
                    if (picture.getValid ())
                    {
                        rectangle = picture.getBounds ();
                        if ((null == clip) || (clip.intersects (rectangle)))
                        {
                            image = picture.getImage ();
                            if (!set.contains (image))
                            {
                                set.add (image);
                                origin = picture.getOrigin ();
                                Watcher observer = new Watcher (
                                    this, image,
                                    origin.x, origin.y, 0, 0);
                                width = image.getWidth (observer);
                                height = image.getHeight (observer);
                                if (prepareImage (image, observer))
                                    if (!observer.drawn)
                                        graphics.drawImage (image,
                                            origin.x, origin.y,
                                            origin.x + width, origin.y + height,
                                            0, 0, width, height,
                                            null);
                                    else
                                        System.out.println ("* *** *** ** * already drawn!!!");
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Get the preferred size of the component.
     * @return The dimension of this component.
     */
    public Dimension getPreferredSize ()
    {
        if (null == mPreferredSize)
            setPreferredSize (calculatePreferredSize ());
        else
            if ((0 == mPreferredSize.width) || (0 == mPreferredSize.height))
                setPreferredSize (calculatePreferredSize ());
        return (mPreferredSize);
    }

    /**
     * Sets the preferred size of this component.
     * @param dimension The new value to use for
     * <code>getPreferredSize()</code> until recalculated.
     */
    public void setPreferredSize (final Dimension dimension)
    {
        mPreferredSize = dimension;
    }

    /**
     * Compute the preferred size of the component.
     * Computes the minimum bounding rectangle covering all the pictures in
     * the panel. It then does some funky stuff to handle
     * embedding in the view port of a scroll pane, basically asking
     * up the ancestor heirarchy what size is available, and filling it.
     * @return The optimal dimension for this component.
     */
    protected Dimension calculatePreferredSize ()
    {
        Iterator<Tile> iterator;
        int x;
        int y;
        Tile picture;
        Rectangle rectangle;
        Component parent;
        Insets insets;
        Dimension ret;

        iterator = mMosaic.getTiles ();
        x = 0;
        y = 0;
        picture = null;
        while (iterator.hasNext ())
        {
            picture = iterator.next ();
            rectangle = picture.getBounds ();
            if (rectangle.x + rectangle.width > x)
                x = rectangle.x + rectangle.width;
            if (rectangle.y + rectangle.height > y)
                y = rectangle.y + rectangle.height;
        }
        parent = getParent ();
        if (parent instanceof JViewport)
        {
            ret = parent.getSize ();
            insets = ((JViewport)parent).getInsets ();
            ret.width -= insets.left + insets.right;
            ret.height -= insets.top + insets.bottom;
            if ((0 != ret.width) || (0 != ret.height))
                ret.width -= 2; // ... I dunno why, it just needs it
            if (ret.width < x)
                ret.width = x;
            if (ret.height < y)
                ret.height = y;
        }
        else
        {
            insets = getInsets ();
            x += insets.left + insets.right;
            y += insets.top + insets.bottom;
            ret = new Dimension (x, y);
        }

        return (ret);
    }

    //
    // MouseListener Interface
    //

    /**
     * Invoked when the mouse button has been clicked
     * (pressed and released) on a component.
     * <i>Not used.</i>
     * @param event The object providing details of the mouse event.
     */
    public void mouseClicked (final MouseEvent event)
    {
    }

    /**
     *Invoked when a mouse button has been released on a component.
     * <i>Not used.</i>
     * @param event The object providing details of the mouse event.
     */
    public void mouseReleased (final MouseEvent event)
    {
    }

    /**
     * Invoked when the mouse enters a component.
     * <i>Not used.</i>
     * @param event The object providing details of the mouse event.
     */
    public void mouseEntered (final MouseEvent event)
    {
    }

    /**
     * Invoked when the mouse exits a component.
     * <i>Not used.</i>
     * @param event The object providing details of the mouse event.
     */
    public void mouseExited (final MouseEvent event)
    {
    }

    /**
     * Handle left click on a picture by bringing it to the top.
     * @param event The object providing details of the mouse event.
     */
    public void mousePressed (final MouseEvent event)
    {
        Tile tile;

        if (!event.isMetaDown ())
        {
            tile = mMosaic.tileAt (event.getX (), event.getY ());
            if (null != tile)
                bringToTop (tile);
        }
    }

    //
    // Scrollable interface
    //

    /**
     * Returns the preferred size of the viewport for a view component.
     * For example the preferredSize of a JList component is the size
     * required to accommodate all of the cells in its list however the
     * value of preferredScrollableViewportSize is the size required for
     * JList.getVisibleRowCount() rows.   A component without any properties
     * that would effect the viewport size should just return
     * getPreferredSize() here.
     *
     * @return The preferredSize of a JViewport whose view is this Scrollable.
     * @see JViewport#getPreferredSize
     */
    public Dimension getPreferredScrollableViewportSize ()
    {
        return (getPreferredSize ());
    }


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  Ideally,
     * components should handle a partially exposed row or column by
     * returning the distance required to completely expose the item.
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a unit scroll.
     *
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or
     * SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left,
     * greater than zero for down/right.
     * @return The "unit" increment for scrolling in the specified direction.
     *         This value should always be positive.
     */
    public int getScrollableUnitIncrement (
        final Rectangle visibleRect,
        final int orientation,
        final int direction)
    {
        return (UNIT_INCREMENT);
    }


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation.
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a block scroll.
     *
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or
     * SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left,
     * greater than zero for down/right.
     * @return The "block" increment for scrolling in the specified direction.
     *         This value should always be positive.
     */
    public int getScrollableBlockIncrement (
        final Rectangle visibleRect,
        final int orientation,
        final int direction)
    {
        return (BLOCK_INCREMENT);
    }


    /**
     * Return true if a viewport should always force the width of this
     * <code>Scrollable</code> to match the width of the viewport.
     * For example a normal
     * text view that supported line wrapping would return true here, since it
     * would be undesirable for wrapped lines to disappear beyond the right
     * edge of the viewport.  Note that returning true for a Scrollable
     * whose ancestor is a JScrollPane effectively disables horizontal
     * scrolling.
     * <p>
     * Scrolling containers, like JViewport, will use this method each
     * time they are validated.
     *
     * @return <code>true</code> if a viewport should force the Scrollables
     * width to match its own.
     */
    public boolean getScrollableTracksViewportWidth ()
    {
        return (false);
    }

    /**
     * Return true if a viewport should always force the height of this
     * Scrollable to match the height of the viewport.  For example a
     * columnar text view that flowed text in left to right columns
     * could effectively disable vertical scrolling by returning
     * true here.
     * <p>
     * Scrolling containers, like JViewport, will use this method each
     * time they are validated.
     *
     * @return <code>true</code> if a viewport should force the Scrollables
     * height to match its own.
     */
    public boolean getScrollableTracksViewportHeight ()
    {
        return (false);
    }

    //
    // ComponentListener interface
    //

    /**
     * Invoked when the container's size changes.
     * Un-caches the preferred size.
     * @param event The resize event.
     */
    public void componentResized (final ComponentEvent event)
    {
        setPreferredSize (null);
    }

    /**
     * Invoked when the component's position changes.
     * <i>Not used.</I>
     * @param event The component event.
     */
    public void componentMoved (final ComponentEvent event)
    {
    }

    /**
     * Invoked when the component has been made visible.
     * <i>Not used.</I>
     * @param event The component event.
     */
    public void componentShown (final ComponentEvent event)
    {
    }

    /**
     * Invoked when the component has been made invisible.
     * <i>Not used.</I>
     * @param event The component event.
     */
    public void componentHidden (final ComponentEvent event)
    {
    }

    //
    // HierarchyListener interface
    //

    /**
     * Handles this components ancestor being added to a container.
     * Registers this component as a listener for size changes on the
     * ancestor so that we may un-cache the prefereed size and force
     * a recalculation.
     * @param event The heirarchy event.
     */
    public void hierarchyChanged (final HierarchyEvent event)
    {
        if (0 != (event.getChangeFlags () & HierarchyEvent.PARENT_CHANGED))
        {
            Component dad = event.getChanged ();
            Component parent = getParent ();
            if ((null != parent) && (parent.getParent () == dad))
                dad.addComponentListener (this);
        }
    }
}

/*
 * Revision Control Modification History
 *
 * $Log: PicturePanel.java,v $
 * Revision 1.2  2005/04/12 11:27:41  derrickoswald
 * Documentation revamp part two.
 *
 * Revision 1.1  2003/09/21 18:20:56  derrickoswald
 * Thumbelina
 * Created a lexer GUI application to extract images behind thumbnails.
 * Added a task in the ant build script - thumbelina - to create the jar file.
 * You need JDK 1.4.x to build it.  It can be run on JDK 1.3.x in crippled mode.
 * Usage: java -Xmx256M thumbelina.jar [URL]
 *
 *
 */
