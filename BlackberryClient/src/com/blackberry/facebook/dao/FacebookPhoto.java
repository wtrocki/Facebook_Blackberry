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

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Comment;
import com.blackberry.facebook.inf.Photo;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.Tag;
import com.blackberry.util.DateUtils;

public class FacebookPhoto extends FacebookObject implements Photo {

	public FacebookPhoto(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public Profile getFrom() {
		Profile result = null;
		try {
			result = fb.getProfile(jsonObject.optJSONObject("from"));
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Tag[] getTags() {
		Tag[] result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("tags");
			if ((jo != null) && (jo.length() > 0)) {
				JSONArray ja = jo.optJSONArray("data");
				if ((ja != null) && (ja.length() > 0)) {
					result = new Tag[ja.length()];
					for (int i = 0; i < ja.length(); i++) {
						result[i] = fb.getTag(ja.optJSONObject(i));
					}
				}
			}

		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	public Bitmap getIcon(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Bitmap[] result = new Bitmap[1];
						result[0] = getIcon();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			String url;
			Bitmap result = null;

			try {
				url = jsonObject.optString("icon");
				if ((url == null) || url.trim().equals("")) {
					return null;
				}
				byte[] data = fb.readRaw(url, false);
				if ((data != null) && (data.length > 0)) {
					result = Bitmap.createBitmapFromBytes(data, 0, data.length, 1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public Bitmap getIcon() {
		return getIcon(null, null);
	}

	public String getSource() {
		return jsonObject.optString("source");
	}

	public int getHeight() {
		return jsonObject.optInt("height");
	}

	public int getWidth() {
		return jsonObject.optInt("width");
	}

	public String getLink() {
		return jsonObject.optString("link");
	}

	public Date getCreatedTime() {
		return DateUtils.parse(getCreatedTimeAsString());
	}

	public String getCreatedTimeAsString() {
		return jsonObject.optString("created_time");
	}

	public Date getUpdatedTime() {
		return DateUtils.parse(getUpdatedTimeAsString());
	}

	public String getUpdatedTimeAsString() {
		return jsonObject.optString("updated_time");
	}

	// Connections

	public Comment[] getComments(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Comment[] result = getComments();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Comment[] result = null;
			try {
				JSONObject jo = fb.read(getId() + "/comments");
				if ((jo != null) && (jo.length() > 0)) {
					JSONArray ja = jo.optJSONArray("data");
					if ((ja != null) && (ja.length() > 0)) {
						result = new Comment[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							result[i] = fb.getComment(ja.optJSONObject(i));
						}
					}
				}

			} catch (FacebookException e) {
				e.printStackTrace();
			}

			return result;
		}
	}

	public Comment[] getComments() {
		return getComments(null, null);
	}

	public Profile[] getLikedUsers(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Profile[] result = getLikedUsers();
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

	public Profile[] getLikedUsers() {
		return getLikedUsers(null, null);
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
				if ((pPictureType == Facebook.PictureTypes.NORMAL)) {
					params.put("type", "normal");
				} else if ((pPictureType == Facebook.PictureTypes.THUMBNAIL)) {
					params.put("type", "thumbnail");
				} else if ((pPictureType == Facebook.PictureTypes.ALBUM)) {
					params.put("type", "album");
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

}
