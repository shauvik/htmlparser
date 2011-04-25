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

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Class to track pictures within the frame.
 * Maintains an image, an area and the URL for it.
 */
public class Picture
    extends
        Rectangle
    implements
        Runnable,
        ImageObserver,
        Tile
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Copy buffer size.
     * Resources are moved to disk in chunks this size or less.
     */
    protected final int TRANSFER_SIZE = 4096;

    /**
     * The origin for new points from the zero args constructor.
     */
    public static final Point ORIGIN = new Point (0, 0);

    /**
     * The URL for the picture.
     */
    protected URL mURL;

    /**
     * The object to notify about picture events;
     */
    public PictureListener mListener;

    /**
     * Flag indicating the image is valid.
     */
    protected boolean mValid;

    /**
     * The upper left hand corner of the image.
     * This doesn't change, even if the image is cropped.
     * For example, if the left half of the image is obscured by another,
     * the <code>Rectangle</code> fields <code>x</code>, <code>y</code>,
     * <code>width</code> and <code>height</code> will change, but the
     * origin remains the same.
     */
    protected Point mOrigin;

    /**
     * The lower right hand corner of the image.
     * This doesn't change, even if the image is cropped.
     * For example, if the right half of the image is obscured by another,
     * the <code>Rectangle</code> fields
     * <code>width</code> and <code>height</code> will change, but the
     * extent remains the same.
     */
    protected Point mExtent;

    /**
     * The cached image.
     */
    protected Image mImage;

    /**
     * The local file containing the picture.
     */
    protected File mLocalFile;

    /**
     * Construct a picture over the rectangle given.
     * @param r The coordinates of the area.
     */
    public Picture (URL url, PictureListener listener, final Rectangle r)
    {
//        super (r);
        setIdentity (url);
        mListener = listener;
        setValid (true);
        if (null != r)
        {
            setOrigin (new Point (r.x, r.y));
            setExtent (new Point (r.width, r.height));
        }
        else
        {
            setOrigin (ORIGIN);
            setExtent (ORIGIN);
        }
        mImage = null;
        mLocalFile = null;
    }

    /**
     * Getter for property origin.
     * @return Value of property origin.
     */
    public Point getOrigin ()
    {
        return (mOrigin);
    }

    /**
     * Setter for property origin.
     * @param origin New value of property origin.
     */
    public void setOrigin (final Point origin)
    {
        x = origin.x;
        y = origin.y;
        mOrigin = origin;
    }

    /**
     * Getter for property extent.
     * @return Value of property extent.
     */
    public Point getExtent ()
    {
        return (mExtent);
    }

    /**
     * Setter for property extent.
     * @param extent New value of property extent.
     */
    public void setExtent (final Point extent)
    {
        width = extent.x;
        height = extent.y;
        mExtent = extent;
    }

    /**
     * Getter for property image.
     * @return Value of property image.
     */
    public Image getImage ()
    {
        long size;
        byte[] bytes;
        FileInputStream in;

        if ((null == mImage) && getValid ())
        {
            if (null != mLocalFile)
            {
                size = mLocalFile.length ();
                try
                {
                    if (size > Integer.MAX_VALUE)
                        setValid (false);
                    else
                    {
                        bytes = new byte[(int)size];
                        try
                        {
                            in = new FileInputStream (mLocalFile);
                            in.read (bytes);
                            in.close ();
                            mImage = Toolkit.getDefaultToolkit ().createImage (bytes);
                        }
                        catch (IOException ioe)
                        {
                            System.err.println (getIdentity ().toString () + " is corrupt on disk");
                            setValid (false);
                        }
                    }
                }
                catch (OutOfMemoryError oome)
                {
                    System.err.println (getIdentity ().toString () + " is too big for available memory");
                    setValid (false);
                }
            }
        }

        return (mImage);
    }

    //
    // Runnable interface
    //

    public void run ()
    {
        String suffix;
        File file;
        byte[] data;
        InputStream in;
        FileOutputStream out;
        int read;
        Image image;

        suffix = mURL.toString ();
        Thread.currentThread ().setName (suffix);
        suffix = suffix.substring (suffix.lastIndexOf ("."));
        try
        {
            file = File.createTempFile ("thumbelina", suffix);
            file.deleteOnExit ();
            data = new byte [TRANSFER_SIZE];
            try
            {
                in = mURL.openStream ();
                try
                {
                    out = new FileOutputStream (file);
                    try
                    {
                        while (-1 != (read = in.read (data, 0, data.length)))
                            out.write (data, 0, read);
                        mLocalFile = file;
                        image = getImage ();
                        setExtent (new Point (image.getWidth (this), image.getHeight (this)));
                        if (null != mListener)
                            mListener.pictureReceived (this);
                    }
                    finally
                    {
                        out.close ();
                    }
                }
                catch (FileNotFoundException fnfe)
                {
                    fnfe.printStackTrace ();
                }
                finally
                {
                    in.close ();
                }
            }
            catch (FileNotFoundException fnfe)
            {
                System.err.println ("broken link " + fnfe.getMessage () + " ignored");
            }
        }
        catch (IOException ioe)
        {
            System.err.println ("I/O problem: " + ioe.getMessage ());
            file = null;
        }
    }

    //
    // Tile interface
    //

    /*
     * Returns true if this picture is valid.
     * @return <code>true</code> when the picture has not been marked invalid.
     */
    public boolean getValid ()
    {
        return (mValid);
    }

    /**
     * Sets the validity of the picture.
     * @param valid If <code>true</code> the picture is marked valid,
     * false otherwise.
     */
    public void setValid (boolean valid)
    {
        mValid = valid;
        // but be sure to release the image memory
        mImage = null;
    }

    /**
     * Getter for property identity.
     * @return Value of property identity.
     */
    public Object getIdentity ()
    {
        return (mURL);
    }

    /**
     * Setter for property identity.
     * @param object New value of property identity.
     */
    public void setIdentity (Object object)
    {
        mURL = (URL)object;
    }

    /**
     * Reset the picture to uncropped size.
     */
    public void reset ()
    {
        setBounds (mOrigin.x, mOrigin.y, mExtent.x, mExtent.y);
    }

// for now
    public void setBounds (Rectangle r)
    {
        if ((r.width < 0) || (r.height < 0))
            System.out.println ("oops");
        super.setBounds (r);
    }

    public void setBounds (int x, int y, int width, int height)
    {
        if ((width < 0) || (height < 0))
            System.out.println ("oops");
        super.setBounds (x, y, width, height);
    }

    //
    // ImageObserver interface
    //

    /**
     * This method is called when information about an image which was
     * previously requested using an asynchronous interface becomes
     * available.  Asynchronous interfaces are method calls such as
     * getWidth(ImageObserver) and drawImage(img, x, y, ImageObserver)
     * which take an ImageObserver object as an argument.  Those methods
     * register the caller as interested either in information about
     * the overall image itself (in the case of getWidth(ImageObserver))
     * or about an output version of an image (in the case of the
     * drawImage(img, x, y, [w, h,] ImageObserver) call).
     *
     * <p>This method
     * should return true if further updates are needed or false if the
     * required information has been acquired.  The image which was being
     * tracked is passed in using the img argument.  Various constants
     * are combined to form the infoflags argument which indicates what
     * information about the image is now available.  The interpretation
     * of the x, y, width, and height arguments depends on the contents
     * of the infoflags argument.
     * <p>
     * The <code>infoflags</code> argument should be the bitwise inclusive
     * <b>OR</b> of the following flags: <code>WIDTH</code>,
     * <code>HEIGHT</code>, <code>PROPERTIES</code>, <code>SOMEBITS</code>,
     * <code>FRAMEBITS</code>, <code>ALLBITS</code>, <code>ERROR</code>,
     * <code>ABORT</code>.
     *
     * @param     image   the image being observed.
     * @param     infoflags   the bitwise inclusive OR of the following
     *               flags:  <code>WIDTH</code>, <code>HEIGHT</code>,
     *               <code>PROPERTIES</code>, <code>SOMEBITS</code>,
     *               <code>FRAMEBITS</code>, <code>ALLBITS</code>,
     *               <code>ERROR</code>, <code>ABORT</code>.
     * @param     x   the <i>x</i> coordinate.
     * @param     y   the <i>y</i> coordinate.
     * @param     width    the width.
     * @param     height   the height.
     * @return    <code>false</code> if the infoflags indicate that the
     *            image is completely loaded; <code>true</code> otherwise.
     *
     * @see #WIDTH
     * @see #HEIGHT
     * @see #PROPERTIES
     * @see #SOMEBITS
     * @see #FRAMEBITS
     * @see #ALLBITS
     * @see #ERROR
     * @see #ABORT
     * @see Image#getWidth
     * @see Image#getHeight
     * @see java.awt.Graphics#drawImage
     */
    public synchronized boolean imageUpdate (
        final Image image,
        final int infoflags,
        final int x,
        final int y,
        final int width,
        final int height)
    {
        boolean done;
        boolean error;
        boolean abort;

        mImage = image;
        done = (0 != (infoflags & ImageObserver.ALLBITS));
        abort = (0 != (infoflags & ImageObserver.ABORT));
        error = (0 != (infoflags & ImageObserver.ERROR));
        if (done || abort || error)
            synchronized (mImage)
            {
                if (!done)
                {
                    System.err.println (getIdentity ().toString () + " is invalid");
                    mImage = null;
                    setValid (false);
                }
                else
                {
                    setExtent (new Point (mImage.getWidth (null), mImage.getHeight (null)));
                    if (null != mListener)
                        mListener.pictureReady (this);
                }

            }

        return (!done);
     }

    /**
     * Create a string representation of the picture.
     * @return A string that shows this picture URL and size.
     */
    public String toString ()
    {
        StringBuffer ret;

        ret = new StringBuffer ();
        ret.append (getIdentity ().toString ());
        ret.append ("[x=");
        ret.append (Integer.toString (x));
        ret.append (",y=");
        ret.append (Integer.toString (y));
        ret.append (",width=");
        ret.append (Integer.toString (width));
        ret.append (",height=");
        ret.append (Integer.toString (height));
        ret.append ("]");

        return (ret.toString ());
    }
}

/*
 * Revision Control Modification History
 *
 * $Log: Picture.java,v $
 * Revision 1.2  2003/12/16 02:29:56  derrickoswald
 * Javadoc changes and additions. Stylesheet, overview, build instructions and todo list.
 * Added HTMLTaglet, an inline Javadoc taglet for embedding HTML into javadocs.
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
