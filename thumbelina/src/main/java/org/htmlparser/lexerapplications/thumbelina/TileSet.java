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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class to track tile regions.
 */
public class TileSet
    extends
        java.awt.Canvas
    implements
        java.awt.event.ActionListener,
        java.awt.event.MouseListener,
        java.awt.event.WindowListener
{
    /**
     * The list of Tiles.
     */
    protected ArrayList<Tile> mRegions;

    /**
     * Construct a tile set.
     */
    public TileSet ()
    {
        mRegions = new ArrayList<Tile> ();
    }

    /**
     * Get the number of tiles in this collection.
     * @return The number of tiles showing.
     * Note that the same tile (as determinded by the identity) may be showing
     * (different pieces) in several locations.
     */
    public int getTileCount ()
    {
        return (mRegions.size ());
    }

    public Iterator<Tile> getTiles ()
    {
        return (mRegions.iterator ());
    }

    /**
     * Add a single tile to the list.
     * @param tile The tile to add.
     */
    public void add (final Tile tile)
    {
        ArrayList<Tile> regions; // this will be the new set
        Iterator<Tile> iterator;
        Rectangle intersection;
        ArrayList<Tile> splits;
        Map<Object,ArrayList<Tile>> table;
        Rectangle rectangle;
        Rectangle r;
        int size;
        Tile test;

        regions = new ArrayList<Tile> ();
        table = new HashMap<Object,ArrayList<Tile>> ();
        rectangle = tile.getBounds ();
        for (Tile rover : mRegions)
        {
            if (rover.getValid ())
            {
                ArrayList<Tile> siblings;
                r = rover.getBounds ();
                if (r.intersects (rectangle))
                {
                    intersection = r.intersection (rectangle);
                    if (!intersection.equals (rover))
                    {
                        // incoming lies completely within the existing tile
                        // or touches the existing tile somehow
                        splits = split (tile, rover, false);
                        siblings = table.get (rover.getIdentity ());
                        if (null == siblings)
                        {
                            siblings = new ArrayList<Tile> ();
                            table.put (rover.getIdentity (), siblings);
                        }
                        for (int i = 0; i < splits.size (); i++)
                        {
                            rover = splits.get (i);
                            regions.add (rover);
                            siblings.add (rover);
                        }
                    }
                    else
                        // incoming covers existing... drop the existing tile
                        rover.setValid (false);
                }
                else
                {
                    // no conflict, keep the existing
                    regions.add (rover);
                    siblings = table.get (rover.getIdentity ());
                    if (null == siblings)
                    {
                        siblings = new ArrayList<Tile> ();
                        table.put (rover.getIdentity (), siblings);
                    }
                    siblings.add (rover);
                }
            }
        }
        regions.add (tile);

        // OK, see if we can amalgamate any siblings
        for (ArrayList<Tile> siblings : table.values ())
        {
            for (int i = 0; i < siblings.size (); i++)
            {
                Tile rover;
                
                rover = siblings.get (i);
                r = rover.getBounds ();
                for (int j = i; j < siblings.size (); j++)
                {
                    test = siblings.get (j);
                    rectangle = test.getBounds ();
                    if ((r.x == rectangle.x) && (r.width == rectangle.width))
                    {
                        // x extent matches, check if top and bottom coincide
                        if (r.y == (rectangle.y + rectangle.height))
                        {
                            // tile above rover, set top up
                            r.y = rectangle.y;
                            r.height += rectangle.height;
                            regions.remove (test);
                            siblings.remove (test);
                            j--;
                        }
                        else if (rectangle.y == (r.y + r.height))
                        {
                            // tile below rover, set bottom down
                            r.height += rectangle.height;
                            regions.remove (test);
                            siblings.remove (test);
                            j--;
                        }
                    }
                    else if ((r.y == rectangle.y) && (r.height == rectangle.height))
                    {
                        // y extent matches, check if left and right coincide
                        if (r.x == (rectangle.x + rectangle.width))
                        {
                            // tile to left of rover, set left less
                            r.x = rectangle.x;
                            r.width += rectangle.width;
                            regions.remove (test);
                            siblings.remove (test);
                            j--;
                        }
                        else if (rectangle.x == (r.x + r.width))
                        {
                            // tile to right of rover, set right more
                            r.width += rectangle.width;
                            regions.remove (test);
                            siblings.remove (test);
                            j--;
                        }
                    }
                }
                rover.setBounds (r);
            }
        }
        
        mRegions = regions;
    }

    /**
     * Split the large tile.
     * Strategy: split horizontally (full width strips top and bottom).
     * NOTE: top and bottom make sense only in terms of AWT coordinates.
     * @param small The incoming tile.
     * @param large The encompassing tile. The attributes of this one
     * are propagated to the fragments.
     * @param keep If <code>true</code>, the center area is kept,
     * otherwise discarded.
     * @return The fragments from the large tile.
     */
    private ArrayList<Tile> split (
        final Tile small,
        final Tile large,
        final boolean keep)
    {
        Rectangle rl;
        Rectangle rs;
        Rectangle rectangle;
        Rectangle intersection;
        Tile m;
        ArrayList<Tile> ret;

        ret = new ArrayList<Tile> ();

        rl = large.getBounds ();
        rs = small.getBounds ();
        if (rl.intersects (rs))
        {
            intersection = rl.intersection (rs);

            // if tops don't match split off the top
            if ((intersection.y + intersection.height)
                != (rl.y + rl.height))
            {
                m = (Tile)large.clone ();
                rectangle = m.getBounds ();
                rectangle.y = (intersection.y + intersection.height);
                rectangle.height = (rl.y + rl.height) - rectangle.y;
                m.setBounds (rectangle);
                ret.add (m);
            }

            // if left sides don't match make a left fragment
            if (intersection.x != rl.x)
            {
                m = (Tile)large.clone ();
                rectangle = m.getBounds ();
                rectangle.y = intersection.y;
                rectangle.width = intersection.x - rl.x;
                rectangle.height = intersection.height;
                m.setBounds (rectangle);
                ret.add (m);
            }

            // the center bit
            if (keep)
            {
                m = (Tile)large.clone ();
                rectangle = m.getBounds ();
                rectangle.x = intersection.x;
                rectangle.y = intersection.y;
                rectangle.width = intersection.width;
                rectangle.height = intersection.height;
                m.setBounds (rectangle);
                ret.add (m);
            }

            // if right sides don't match make a right fragment
            if ((intersection.x + intersection.width)
                != (rl.x + rl.width))
            {
                m = (Tile)large.clone ();
                rectangle = m.getBounds ();
                rectangle.x = intersection.x + intersection.width;
                rectangle.y = intersection.y;
                rectangle.width = (rl.x + rl.width) - rectangle.x;
                rectangle.height = intersection.height;
                m.setBounds (rectangle);
                ret.add (m);
            }

            // if bottoms don't match split off the bottom
            if (intersection.y != rl.y)
            {
                m = (Tile)large.clone ();
                rectangle = m.getBounds ();
                rectangle.height = (intersection.y - rl.y);
                m.setBounds (rectangle);
                ret.add (m);
            }
        }

        return (ret);
    }

    /**
     * Find the Tile at position x,y
     * @param x The x coordinate of the point to examine.
     * @param y The y coordinate of the point to examine.
     * @return The tile at that point, or <code>null</code>
     * if there are none.
     */
    public Tile tileAt (final int x, final int y)
    {
        Tile ret;

        ret = null;

        for (Tile m : mRegions)
        {
            if (m.getBounds ().contains (x, y))
            {
                ret = m;
                break;
            }
        }

        return (ret);
    }

    /**
     * Move the given tile to the top of the Z order.
     * The tile is reset to it's original size and all fragments are discarded.
     * @param tile The tile to bring to the top.
     */
    public void bringToTop (final Tile tile)
    {
        Iterator<Tile> iterator;
        Tile m;

        for (iterator = mRegions.iterator (); iterator.hasNext (); )
        {
            m = iterator.next ();
            if (tile.getIdentity ().equals (m.getIdentity ()))
                iterator.remove ();
        }
        tile.reset ();
        add (tile);

    }

    //
    // Unit test.
    //

    // also need to add:
    //    extends
    //        java.awt.Canvas
    //    implements
    //        java.awt.event.ActionListener,
    //        java.awt.event.MouseListener,
    //        java.awt.event.WindowListener
    // to the class definition

    static final java.awt.Color[] mColours =
    {
        java.awt.Color.blue,
        java.awt.Color.cyan,
        java.awt.Color.gray,
        java.awt.Color.green,
        java.awt.Color.orange,
        java.awt.Color.pink,
        java.awt.Color.red,
        java.awt.Color.yellow,
        java.awt.Color.lightGray,
        java.awt.Color.darkGray,
    };

    java.awt.Point origin;
    Rectangle last;

    public void setStatus (String text)
    {
        java.awt.Container container = getParent ();
        java.awt.Component[] children = container.getComponents ();
        for (int i = 0; i < children.length; i++)
            if (children[i] instanceof java.awt.TextField)
            {
                ((java.awt.TextField)children[i]).setText (text);
                break;
            }
    }

    public boolean isVerbose ()
    {
        boolean ret;

        ret = false;

        java.awt.Container container = getParent ();
        java.awt.Frame frame = (java.awt.Frame)container;
        java.awt.MenuBar menubar = frame.getMenuBar ();
        java.awt.Menu menu = menubar.getMenu (0);
        for (int i = 0; i < menu.getItemCount (); i++)
            if (menu.getItem (i) instanceof java.awt.CheckboxMenuItem)
            {
                ret = ((java.awt.CheckboxMenuItem)menu.getItem (i)).getState ();
                break;
            }
        
        return (ret);
    }

    void addTile (Rectangle rectangle)
    {
        try
        {
            int count = Integer.parseInt (getName ());
            Tile m = new Picture (new java.net.URL ("http://localhost/image#" + count), null, rectangle);
            add (m);
            repaint ();
            setStatus ("" + getTileCount ());
            count++;
            setName ("" + count);
        }
        catch (java.net.MalformedURLException murle)
        {
            murle.printStackTrace ();
        }
    }

    void checkOverlap (java.awt.Graphics graphics)
    {
        Tile m;
        Rectangle rectangle;
        Tile _m;
        Rectangle r;

        graphics.setColor (java.awt.Color.magenta);
        for (int i = 0; i < mRegions.size (); i++)
        {
            m = mRegions.get (i);
            rectangle = m.getBounds ();
            for (int j = i + 1; j < mRegions.size (); j++)
            {
                _m = mRegions.get (j);
                r = _m.getBounds ();
                if (rectangle.intersects (r))
                {
                    r = rectangle.intersection (r);
                    System.out.println (
                        "overlap ("
                        + r.x
                        + ","
                        + r.y
                        + ") ("
                        + (r.x + r.width)
                        + ","
                        + (r.y + r.height)
                        + ")");
                    graphics.fillRect (r.x, r.y, r.width + 1, r.height + 1);
                }
            }
        }
    }

    void lift (int x, int y)
    {
        Tile m;

        m = tileAt (x, y);
        if (null != m)
        {
            bringToTop (m);
            repaint ();
            setStatus ("" + getTileCount ());
        }
    }

    String getDetails (int x, int y)
    {
        Tile m;
        String ret;

        ret = null;

        m = tileAt (x, y);
        if (null == m)
            ret = "";
        else
            ret = m.toString ();

        return (ret);
    }

    //
    // Component overrides
    //

    public void update (java.awt.Graphics graphics)
    {
        paint (graphics);
    }

    public void paint (java.awt.Graphics graphics)
    {
        java.awt.Dimension size = getSize ();
        graphics.setColor (getBackground ());
        graphics.fillRect (0, 0, size.width + 1, size.height + 1);

        if (0 == mRegions.size ())
        {
            graphics.setColor (getForeground ());
            graphics.drawString (
                "Click and drag to create a tile.", 10, 20);
            graphics.drawString (
                "Right click a tile for details.", 10, 40);
            graphics.drawString (
                "Shift right click a tile to bring to top.", 10, 60);
        }
        else
        {
            for (Tile tile : mRegions)
            {
                String url = ((java.net.URL)tile.getIdentity ()).toString ();
                int n = url.indexOf ('#');
                n = Integer.parseInt (url.substring (n + 1));
                java.awt.Color colour = mColours[n % mColours.length];
                graphics.setColor (colour);
                Rectangle m = tile.getBounds ();
                graphics.fillRect (m.x, m.y, m.width + 1, m.height + 1);
                graphics.setColor (java.awt.Color.black);
                graphics.drawRect (m.x, m.y, m.width, m.height);
            }
            checkOverlap (graphics);
        }
    }

    //
    // WindowListener interface
    //

    public void windowOpened (java.awt.event.WindowEvent e) {}

    public void windowClosing (java.awt.event.WindowEvent e)
    {
        System.exit (0);
    }

    public void windowClosed (java.awt.event.WindowEvent e) {}

    public void windowIconified (java.awt.event.WindowEvent e) {}

    public void windowDeiconified (java.awt.event.WindowEvent e) {}

    public void windowActivated (java.awt.event.WindowEvent e) {}

    public void windowDeactivated (java.awt.event.WindowEvent e) {}

    //
    // ActionListener interface
    //

    public void actionPerformed (java.awt.event.ActionEvent event)
    {
        java.awt.MenuItem item = (java.awt.MenuItem)event.getSource ();
        if (item.getName ().equals ("repeat"))
            addTile (last);
        else if (item.getName ().equals ("clear"))
        {
            mRegions = new ArrayList<Tile> ();
            repaint ();
        }
    }

    //
    // MouseListener Interface
    //

    public void mouseClicked (java.awt.event.MouseEvent event)
    {
        if (isVerbose ())
            System.out.println ("DrawTarget.mouseClicked " + event);
    }

    public void mouseReleased (java.awt.event.MouseEvent event)
    {
        if (isVerbose ())
            System.out.println ("DrawTarget.mouseReleased " + event);
        if (null != origin)
        {
            last = new Rectangle (
                Math.min (origin.x, event.getX ()),
                Math.min (origin.y, event.getY ()),
                Math.abs (event.getX () - origin.x),
                Math.abs (event.getY () - origin.y));
            addTile (last);
            origin = null;
        }
    }

    public void mouseEntered (java.awt.event.MouseEvent event)
    {
        if (isVerbose ())
            System.out.println ("DrawTarget.mouseEntered " + event);
    }

    public void mouseExited (java.awt.event.MouseEvent event)
    {
        if (isVerbose ())
            System.out.println ("DrawTarget.mouseExited " + event);
    }

    public void mousePressed (java.awt.event.MouseEvent event)
    {
        if (isVerbose ())
            System.out.println ("DrawTarget.mousePressed " + event);
        if (event.isShiftDown () && event.isMetaDown ())
            lift (event.getX (), event.getY ());
        else if (event.isMetaDown ())
            setStatus (getDetails (event.getX (), event.getY ()));
        else
            origin = new java.awt.Point (event.getX (), event.getY ());
    }

    /**
     * Visual unit test for the TileSet class.
     * @param args <em>Ignored.</em>
     */
    public static void main (String[] args)
    {
        java.awt.Frame frame;
        java.awt.MenuBar menuMain;
        java.awt.Menu options;
        java.awt.CheckboxMenuItem verbose;
        java.awt.MenuItem repeat;
        java.awt.MenuItem clear;
        java.awt.TextField status;

        frame = new java.awt.Frame ();
        frame.setSize (400,400);
        menuMain = new java.awt.MenuBar();
        options = new java.awt.Menu ("Options");
        verbose = new java.awt.CheckboxMenuItem ("Verbose");
        options.add (verbose);
        repeat = new java.awt.MenuItem("Repeat");
        repeat.setName ("repeat");
        options.add (repeat);
        clear = new java.awt.MenuItem("Clear");
        clear.setName ("clear");
        options.add (clear);

        menuMain.add (options);
        frame.setMenuBar (menuMain);

        TileSet buffy = new TileSet ();
        buffy.setBackground (java.awt.Color.lightGray.brighter ());
        buffy.setVisible (true);
        buffy.setName ("0");

        frame.add (buffy, "Center");
        status = new java.awt.TextField ();
        frame.add (status, "South");

        frame.addWindowListener (buffy);
        buffy.addMouseListener (buffy);
        repeat.addActionListener (buffy);
        clear.addActionListener (buffy);

        frame.setVisible (true);

    }
}

/*
 * Revision Control Modification History
 *
 * $Log: TileSet.java,v $
 * Revision 1.2  2004/07/31 16:42:30  derrickoswald
 * Remove unused variables and other fixes exposed by turning on compiler warnings.
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
