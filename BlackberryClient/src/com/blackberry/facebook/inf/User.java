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

public interface User extends com.blackberry.facebook.inf.Object {

	// Properties

	public String getName();

	public String getFirstName();

	public String getLastName();

	public String getGender();

	public String getLocale();

	public String getLink();

	public String getThirdPartyId();

	public double getTimezone();

	public Date getUpdatedTime();

	public String getUpdatedTimeAsString();

	public boolean getVerified();

	public String getAbout();

	public String getBio();

	public Date getBirthday();

	public String getBirthdayAsString();

	public Education[] getEducation();

	public String getEmail();

	public Profile getHometown();

	public String getHometownName();

	public String[] getInterestedIn();

	public Profile getLocation();

	public String getLocationName();

	public String[] getMeetingFor();

	public String getPolitical();

	public String getQuotes();

	public String getRelationshipStatus();

	public String getReligion();

	public Profile getSignificantOther();

	public String getWebsite();

	public Work[] getWork();

	// Connections

	public Bitmap getPicture(int pictureType);

	public Bitmap getPicture(final int pPictureType, final AsyncCallback listener, final java.lang.Object state);

	public Profile[] getFriends();

	public Profile[] getFriends(final AsyncCallback listener, final java.lang.Object state);

	// public ??? getAccounts();
	// public ??? getAppRequests();
	// public ??? getActivities();

	public Album[] getAlbums();

	public Album[] getAlbums(final AsyncCallback listener, final java.lang.Object state);

	// public ??? getBooks();

	public Checkin[] getCheckins();

	public Checkin[] getCheckins(final AsyncCallback listener, final java.lang.Object state);

	public Event[] getEvents();

	public Event[] getEvents(final AsyncCallback listener, final java.lang.Object state);

	public Post[] getFeed();

	public Post[] getFeed(final AsyncCallback listener, final java.lang.Object state);

	public FriendList[] getFriendLists();

	public FriendList[] getFriendLists(final AsyncCallback listener, final java.lang.Object state);

	public Post[] getHome();

	public Post[] getHome(final AsyncCallback listener, final java.lang.Object state);

	public Thread[] getInbox();

	public Thread[] getInbox(final AsyncCallback listener, final java.lang.Object state);

	public Interest[] getInterests();

	public Interest[] getInterests(final AsyncCallback listener, final java.lang.Object state);

	public Profile[] getLikedPages();

	public Profile[] getLikedPages(final AsyncCallback listener, final java.lang.Object state);

	public Link[] getLinks();

	public Link[] getLinks(final AsyncCallback listener, final java.lang.Object state);

	// public ??? getMovies();
	// public ??? getMusic();

	public Note[] getNotes();

	public Note[] getNotes(final AsyncCallback listener, final java.lang.Object state);

	public Message[] getOutbox();

	public Message[] getOutbox(final AsyncCallback listener, final java.lang.Object state);

	public Photo[] getPhotos();

	public Photo[] getPhotos(final AsyncCallback listener, final java.lang.Object state);

	public Post[] getPosts();

	public Post[] getPosts(final AsyncCallback listener, final java.lang.Object state);

	public Status[] getStatuses();

	public Status[] getStatuses(final AsyncCallback listener, final java.lang.Object state);

	// public ??? getTagged();
	// public ??? getTelevision();

	public Message[] getUpdates();

	public Message[] getUpdates(final AsyncCallback listener, final java.lang.Object state);

	public Video[] getVideos();

	public Video[] getVideos(final AsyncCallback listener, final java.lang.Object state);

	// Publishing

	public String createAlbum(String pName, String pMessage);

	public String createAlbum(final String pName, final String pMessage, final AsyncCallback listener, final java.lang.Object state);

	public String checkin(String pMessage, String pPageId, double latitude, double longitude, User[] pTags);

	public String checkin(final String pMessage, final String pPageId, final double latitude, final double longitude, final User[] pTags, final AsyncCallback listener, final java.lang.Object state);

	public String createEvent(String pName, Date pStartTime, Date pEndTime);

	public String createEvent(String pName, Date pStartTime, Date pEndTime, final AsyncCallback listener, final java.lang.Object state);

	public String createFriendList(String pName);

	public String createFriendList(String pName, final AsyncCallback listener, final java.lang.Object state);

	public String publishStatus(String pMessage);

	public String publishStatus(final String pMessage, final AsyncCallback listener, final java.lang.Object state);

	public String publishPost(String pMessage, String pLink, String pPicture, String pName, String pCaption, String pDescription, String pSource);

	public String publishPost(final String pMessage, final String pLink, final String pPicture, final String pName, final String pCaption, final String pDescription, final String pSource, final AsyncCallback listener, final java.lang.Object state);

	public String publishNote(String pSubject, String pMessage);

	public String publishNote(String pSubject, String pMessage, final AsyncCallback listener, final java.lang.Object state);

	public String publishPhoto(EncodedImage pPhoto, String pMessage);

	public String publishPhoto(final EncodedImage pPhoto, final String pMessage, final AsyncCallback listener, final java.lang.Object state);

}