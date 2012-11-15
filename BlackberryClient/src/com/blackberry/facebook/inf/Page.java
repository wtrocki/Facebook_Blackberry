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

import net.rim.device.api.system.Bitmap;

import com.blackberry.facebook.AsyncCallback;

public interface Page extends com.blackberry.facebook.inf.Object {

	// Properties

	public String getName();

	public String getCategory();

	public int getLikes();

	public String getLink();

	public String getWebsite();

	public String getUserName();

	public String getFounded();

	public String getCompanyOverview();

	public String getMission();

	public int getFanCount();

	public boolean isCommunityPage();

	public Location getLocation();

	// Connections

	public Post[] getFeed();

	public Post[] getFeed(final AsyncCallback listener, final java.lang.Object state);

	public Bitmap getPicture(int pType);

	public Bitmap getPicture(final int pPictureType, final AsyncCallback listener, final java.lang.Object state);

	// public ??? getTagged();
	public Link[] getLinks();

	public Link[] getLinks(final AsyncCallback listener, final java.lang.Object state);

	public Photo[] getPhotos();

	public Photo[] getPhotos(final AsyncCallback listener, final java.lang.Object state);

	public Group[] getGroups();

	public Group[] getGroups(final AsyncCallback listener, final java.lang.Object state);

	public Album[] getAlbums();

	public Album[] getAlbums(final AsyncCallback listener, final java.lang.Object state);

	public Status[] getStatuses();

	public Status[] getStatuses(final AsyncCallback listener, final java.lang.Object state);

	public Video[] getVideos();

	public Video[] getVideos(final AsyncCallback listener, final java.lang.Object state);

	public Note[] getNotes();

	public Note[] getNotes(final AsyncCallback listener, final java.lang.Object state);

	public Post[] getPosts();

	public Post[] getPosts(final AsyncCallback listener, final java.lang.Object state);

	public Event[] getEvents();

	public Event[] getEvents(final AsyncCallback listener, final java.lang.Object state);

	public Checkin[] getCheckins();

	public Checkin[] getCheckins(final AsyncCallback listener, final java.lang.Object state);

	// Publishing

	public String createAlbum(String pName, String pMessage);

	public String createAlbum(final String pName, final String pMessage, final AsyncCallback listener, final java.lang.Object state);

}