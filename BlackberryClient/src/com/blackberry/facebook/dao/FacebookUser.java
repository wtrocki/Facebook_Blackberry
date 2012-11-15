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
package com.blackberry.facebook.dao;

import java.util.Date;
import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Album;
import com.blackberry.facebook.inf.Checkin;
import com.blackberry.facebook.inf.Education;
import com.blackberry.facebook.inf.Event;
import com.blackberry.facebook.inf.FriendList;
import com.blackberry.facebook.inf.Interest;
import com.blackberry.facebook.inf.Link;
import com.blackberry.facebook.inf.Message;
import com.blackberry.facebook.inf.Note;
import com.blackberry.facebook.inf.Photo;
import com.blackberry.facebook.inf.Post;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.Status;
import com.blackberry.facebook.inf.Thread;
import com.blackberry.facebook.inf.User;
import com.blackberry.facebook.inf.Video;
import com.blackberry.facebook.inf.Work;
import com.blackberry.util.DateUtils;

public class FacebookUser extends FacebookObject implements User {

	public FacebookUser(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	public String getFirstName() {
		return jsonObject.optString("first_name");
	}

	public String getLastName() {
		return jsonObject.optString("last_name");
	}

	public String getGender() {
		return jsonObject.optString("gender");
	}

	public String getLocale() {
		return jsonObject.optString("locale");
	}

	public String getLink() {
		return jsonObject.optString("link");
	}

	public String getThirdPartyId() {
		return jsonObject.optString("third_party_id");
	}

	public double getTimezone() {
		return Double.parseDouble(jsonObject.optString("timezone"));
	}

	public Date getUpdatedTime() {
		return DateUtils.parse(getUpdatedTimeAsString());
	}

	public String getUpdatedTimeAsString() {
		return jsonObject.optString("updated_time");
	}

	public boolean getVerified() {
		return jsonObject.optBoolean("verified");
	}

	public String getAbout() {
		return jsonObject.optString("about");
	}

	public String getBio() {
		return jsonObject.optString("bio");
	}

	public Date getBirthday() {
		// TODO
		// return DateUtils.parse(getBirthdayAsString());
		return null;
	}

	public String getBirthdayAsString() {
		return jsonObject.optString("birthday");
	}

	public Education[] getEducation() {
		Education[] result = null;
		try {
			JSONArray ja = jsonObject.optJSONArray("education");
			if ((ja != null) && (ja.length() > 0)) {
				result = new Education[ja.length()];
				for (int i = 0; i < ja.length(); i++) {
					result[i] = fb.getEducation(ja.optJSONObject(i));
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getEmail() {
		return jsonObject.optString("email");
	}

	public Profile getHometown() {
		Profile result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("hometown");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getProfile(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getHometownName() {
		Profile hometown = getHometown();
		if (hometown != null) {
			return hometown.getName();
		} else {
			return null;
		}
	}

	public String[] getInterestedIn() {
		JSONArray ja = jsonObject.optJSONArray("interested_in");
		if ((ja != null) && (ja.length() > 0)) {
			String[] result = new String[ja.length()];
			for (int i = 0; i < ja.length(); i++) {
				result[i] = ja.optString(i);
			}
			return result;
		} else {
			return null;
		}
	}

	public Profile getLocation() {
		Profile result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("location");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getProfile(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getLocationName() {
		Profile location = getLocation();
		if (location != null) {
			return location.getName();
		} else {
			return null;
		}
	}

	public String[] getMeetingFor() {
		JSONArray ja = jsonObject.optJSONArray("meeting_for");
		if ((ja != null) && (ja.length() > 0)) {
			String[] result = new String[ja.length()];
			for (int i = 0; i < ja.length(); i++) {
				result[i] = ja.optString(i);
			}
			return result;
		} else {
			return null;
		}
	}

	public String getPolitical() {
		return jsonObject.optString("political");
	}

	public String getQuotes() {
		return jsonObject.optString("quotes");
	}

	public String getRelationshipStatus() {
		return jsonObject.optString("relationship_status");
	}

	public String getReligion() {
		return jsonObject.optString("religion");
	}

	public Profile getSignificantOther() {
		Profile result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("significant_other");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getProfile(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getWebsite() {
		return jsonObject.optString("website");
	}

	public Work[] getWork() {
		Work[] result = null;
		try {
			JSONArray ja = jsonObject.optJSONArray("work");
			if ((ja != null) && (ja.length() > 0)) {
				result = new Work[ja.length()];
				for (int i = 0; i < ja.length(); i++) {
					result[i] = fb.getWork(ja.optJSONObject(i));
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	// Connections

	public Bitmap getPicture(final int pPictureType, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Bitmap[] result = new Bitmap[1];
						result[0] = getPicture(pPictureType);
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String path;
			Hashtable params;
			Bitmap result = null;

			try {
				path = getId() + "/picture";
				params = new Hashtable();
				if ((pPictureType == Facebook.PictureTypes.SMALL)) {
					params.put("type", "small");
				} else if ((pPictureType == Facebook.PictureTypes.NORMAL)) {
					params.put("type", "normal");
				} else if ((pPictureType == Facebook.PictureTypes.LARGE)) {
					params.put("type", "large");
				} else if ((pPictureType == Facebook.PictureTypes.SQUARE)) {
					params.put("type", "square");
				} else {
					params.put("type", "normal");
				}

				byte[] data = fb.readRaw(path, params, true, true);
				if ((data != null) && (data.length > 0)) {
					result = Bitmap.createBitmapFromBytes(data, 0, data.length, 1);
				}

			} catch (Throwable t) {
				t.printStackTrace();
			}
			return result;
		}
	}

	public Bitmap getPicture(int pPictureType) {
		return getPicture(pPictureType, null, null);
	}

	public Profile[] getFriends(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Profile[] result = getFriends();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Profile[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/friends");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Profile[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getProfile(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Profile[] getFriends() {
		return getFriends(null, null);
	}

	public FriendList[] getFriendLists(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						FriendList[] result = getFriendLists();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			FriendList[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/friendlists");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new FriendList[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getFriendList(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public FriendList[] getFriendLists() {
		return getFriendLists(null, null);
	}

	public Status[] getStatuses() {
		return getStatuses(null, null);
	}

	public Status[] getStatuses(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Status[] result = getStatuses();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Status[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/statuses");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Status[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getStatus(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Checkin[] getCheckins(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Checkin[] result = getCheckins();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Checkin[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/checkins");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Checkin[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getCheckin(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Checkin[] getCheckins() {
		return getCheckins(null, null);
	}

	public String publishStatus(final String pMessage, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						String[] result = new String[1];
						result[0] = publishStatus(pMessage);
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String result = null;
			try {
				JSONObject requestObject = new JSONObject().put("message", pMessage);
				JSONObject responseObject = fb.write(getId() + "/feed", requestObject, true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error") || !responseObject.has("id")) {
					result = null;
				} else {
					result = responseObject.optString("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public String publishStatus(String pMessage) {
		return publishStatus(pMessage, null, null);
	}

	public String publishPost(final String pMessage, final String pLink, final String pPicture, final String pName, final String pCaption, final String pDescription, final String pSource, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						String[] result = new String[1];
						result[0] = publishPost(pMessage, pLink, pPicture, pName, pCaption, pDescription, pSource);
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String result = null;
			try {
				JSONObject requestObject = new JSONObject();
				if (pMessage != null) {
					requestObject.put("message", pMessage);
				}
				if (pLink != null) {
					requestObject.put("link", pLink);
				}
				if (pPicture != null) {
					requestObject.put("picture", pPicture);
				}
				if (pName != null) {
					requestObject.put("name", pName);
				}
				if (pCaption != null) {
					requestObject.put("caption", pCaption);
				}
				if (pDescription != null) {
					requestObject.put("description", pDescription);
				}
				if (pSource != null) {
					requestObject.put("source", pSource);
				}

				JSONObject responseObject = fb.write(getId() + "/feed", requestObject, true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error") || !responseObject.has("id")) {
					result = null;
				} else {
					result = responseObject.optString("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public String publishPost(String pMessage, String pLink, String pPicture, String pName, String pCaption, String pDescription, String pSource) {
		return publishPost(pMessage, pLink, pPicture, pName, pCaption, pDescription, pSource, null, null);
	}

	public Post[] getFeed(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Post[] result = getFeed();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Post[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/feed");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Post[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getPost(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Post[] getFeed() {
		return getFeed(null, null);
	}

	public Post[] getHome(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Post[] result = getHome();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Post[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/home");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Post[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getPost(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Post[] getHome() {
		return getHome(null, null);
	}

	public Album[] getAlbums(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Album[] result = getAlbums();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Album[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/albums");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Album[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getAlbum(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Album[] getAlbums() {
		return getAlbums(null, null);
	}

	public Event[] getEvents() {
		// TODO
		return null;
	}

	public Thread[] getInbox() {
		// TODO
		return null;
	}

	public Interest[] getInterests(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Interest[] result = getInterests();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Interest[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/interests");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Interest[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getInterest(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Interest[] getInterests() {
		return getInterests(null, null);
	}

	public Profile[] getLikedPages(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Profile[] result = getLikedPages();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Profile[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/likes");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Profile[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getProfile(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Profile[] getLikedPages() {
		return getLikedPages(null, null);
	}

	public Link[] getLinks() {
		// TODO
		return null;
	}

	public Note[] getNotes() {
		// TODO
		return null;
	}

	public Message[] getOutbox() {
		// TODO
		return null;
	}

	public Photo[] getPhotos(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Photo[] result = getPhotos();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Photo[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/photos");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Photo[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getPhoto(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Photo[] getPhotos() {
		return getPhotos(null, null);
	}

	public Post[] getPosts(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Post[] result = getPosts();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Post[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/posts");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Post[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getPost(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Post[] getPosts() {
		return getPosts(null, null);
	}

	public Message[] getUpdates() {
		// TODO
		return null;
	}

	public Video[] getVideos(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Video[] result = getVideos();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Video[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/videos");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Video[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getVideo(ja.optJSONObject(i));
						}
					}
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Video[] getVideos() {
		return getVideos(null, null);
	}

	public String createAlbum(final String pName, final String pMessage, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						String[] result = new String[1];
						result[0] = createAlbum(pName, pMessage);
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String result = null;
			try {
				JSONObject requestObject = new JSONObject();
				requestObject.put("name", pName);
				requestObject.put("message", pMessage);

				JSONObject responseObject = fb.write(getId() + "/albums", requestObject, true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error") || !responseObject.has("id")) {
					result = null;
				} else {
					result = responseObject.optString("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public String createAlbum(String pName, String pMessage) {
		return createAlbum(pName, pMessage, null, null);
	}

	public String checkin(final String pMessage, final String pPageId, final double latitude, final double longitude, final User[] pTags, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						String[] result = new String[1];
						result[0] = checkin(pMessage, pPageId, latitude, longitude, pTags);
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String result = null;
			try {
				JSONObject requestObject = new JSONObject();
				if ((pMessage != null) && !pMessage.equals("")) {
					requestObject.put("message", pMessage);
				}
				if ((pPageId != null) && !pPageId.equals("")) {
					requestObject.put("place", pPageId);
				}
				requestObject.put("coordinates", "{\"latitude\":\"" + latitude + "\", \"longitude\":\"" + longitude + "\"}");

				String tagsString = "";
				if ((pTags != null) && (pTags.length > 0)) {
					for (int i = 0; i < pTags.length; i++) {
						if (i != 0) {
							tagsString += ", ";
						}
						tagsString += pTags[i].getId();
					}
					requestObject.put("tags", tagsString);
				}

				JSONObject responseObject = fb.write(getId() + "/checkins", requestObject, true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error") || !responseObject.has("id")) {
					result = null;
				} else {
					result = responseObject.optString("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public String checkin(String pMessage, String pPageId, double latitude, double longitude, User[] pTags) {
		return checkin(pMessage, pPageId, latitude, longitude, pTags, null, null);
	}

	public String createEvent(String pName, Date pStartTime, Date pEndTime) {
		// TODO
		return null;
	}

	public String createFriendList(String pName) {
		// TODO
		return null;
	}

	public String publishNote(String pSubject, String pMessage) {
		// TODO
		return null;
	}

	public String publishPhoto(final EncodedImage pPhoto, final String pMessage, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						String[] result = new String[1];
						result[0] = publishPhoto(pPhoto, pMessage);
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String result = null;
			try {
				JSONObject requestObject = new JSONObject();
				requestObject.put("source", "filename");
				requestObject.put("message", pMessage);

				byte[] imageData = null;
				if ((pPhoto != null) && (pPhoto.getLength() > 0)) {
					imageData = pPhoto.getData();
				}

				JSONObject responseObject = fb.write(getId() + "/photos", requestObject, pPhoto.getMIMEType(), imageData, true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error") || !responseObject.has("id")) {
					result = null;
				} else {
					result = responseObject.optString("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public String publishPhoto(EncodedImage pPhoto, String pMessage) {
		return publishPhoto(pPhoto, pMessage, null, null);
	}

	public Event[] getEvents(AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public Thread[] getInbox(AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public Link[] getLinks(AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public Note[] getNotes(AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public Message[] getOutbox(AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public Message[] getUpdates(AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public String createEvent(String pName, Date pStartTime, Date pEndTime, AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public String createFriendList(String pName, AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

	public String publishNote(String pSubject, String pMessage, AsyncCallback listener, final java.lang.Object state) {
		// TODO
		return null;
	}

}