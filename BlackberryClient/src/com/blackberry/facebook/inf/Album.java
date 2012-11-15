/**
 * Copyright (c) E.Y. Baskoro, Research In Motion Limited.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * This License shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * The name(s) of the above copyright holders shall not be used 
 * in advertising or otherwise to promote the sale, use or other 
 * dealings in this Software without prior written authorization.
 * 
 */
package com.blackberry.facebook.inf;

import java.util.Date;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

import com.blackberry.facebook.AsyncCallback;

public interface Album extends com.blackberry.facebook.inf.Object {

	// Properties

	public Profile getFrom();

	public String getName();

	public String getDescription();

	public String getLocation();

	public String getLink();

	public String getPrivacy();

	public int getCount();

	public Date getCreatedTime();

	public String getCreatedTimeAsString();

	public Date getUpdatedTime();

	public String getUpdatedTimeAsString();

	public String getType();

	// Connections

	public Photo[] getPhotos();

	public Photo[] getPhotos(final AsyncCallback listener, final java.lang.Object state);

	public Comment[] getComments();

	public Comment[] getComments(final AsyncCallback listener, final java.lang.Object state);

	public Bitmap getPicture(int pPictureType);

	public Bitmap getPicture(final int pPictureType, final AsyncCallback listener, final java.lang.Object state);

	public String publishPhoto(EncodedImage pPhoto, String pMessage);

	public String publishPhoto(final EncodedImage pPhoto, final String pMessage, final AsyncCallback listener, final java.lang.Object state);

}