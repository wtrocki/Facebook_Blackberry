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

import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Album;
import com.blackberry.facebook.inf.Checkin;
import com.blackberry.facebook.inf.Event;
import com.blackberry.facebook.inf.Group;
import com.blackberry.facebook.inf.Link;
import com.blackberry.facebook.inf.Location;
import com.blackberry.facebook.inf.Note;
import com.blackberry.facebook.inf.Page;
import com.blackberry.facebook.inf.Photo;
import com.blackberry.facebook.inf.Post;
import com.blackberry.facebook.inf.Status;
import com.blackberry.facebook.inf.Video;
import com.blackberry.util.json.JSONArray;
import com.blackberry.util.json.JSONException;
import com.blackberry.util.json.JSONObject;

public class FacebookPage extends FacebookObject implements Page {

	public FacebookPage(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	public String getCategory() {
		return jsonObject.optString("category");
	}

	public int getLikes() {
		return jsonObject.optInt("likes");
	}

	public Post[] getFeed() {
		return getFeed(null, null);
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

	public String getLink() {
		return jsonObject.optString("link");
	}

	public boolean isCommunityPage() {
		return jsonObject.optBoolean("is_community_page");
	}

	public Location getLocation() {
		Location result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("location");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getLocation(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getFanCount() {
		return jsonObject.optInt("fan_count");
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

	public String getWebsite() {
		return jsonObject.optString("website");
	}

	public String getUserName() {
		return jsonObject.optString("username");
	}

	public String getFounded() {
		return jsonObject.optString("founded");
	}

	public String getCompanyOverview() {
		return jsonObject.optString("company_overview");
	}

	public String getMission() {
		return jsonObject.optString("mission");
	}

	public Link[] getLinks() {
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

	public Group[] getGroups() {
		// TODO
		return null;
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

	public Status[] getStatuses() {
		return getStatuses(null, null);
	}

	public Video[] getVideos() {
		// TODO
		return null;
	}

	public Note[] getNotes() {
		// TODO
		return null;
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

	public Event[] getEvents() {
		// TODO
		return null;
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

	public Link[] getLinks(AsyncCallback listener, java.lang.Object state) {
		// TODO
		return null;
	}

	public Group[] getGroups(AsyncCallback listener, java.lang.Object state) {
		// TODO
		return null;
	}

	public Video[] getVideos(AsyncCallback listener, java.lang.Object state) {
		// TODO
		return null;
	}

	public Note[] getNotes(AsyncCallback listener, java.lang.Object state) {
		// TODO
		return null;
	}

	public Event[] getEvents(AsyncCallback listener, java.lang.Object state) {
		// TODO
		return null;
	}

}
