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

import net.rim.device.api.system.Bitmap;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Comment;
import com.blackberry.facebook.inf.Post;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.util.date.DateUtils;
import com.blackberry.util.json.JSONArray;
import com.blackberry.util.json.JSONObject;

public class FacebookPost extends FacebookObject implements Post {

	public FacebookPost(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public int getLikes() {
		int result = 0;
		JSONObject jo = jsonObject.optJSONObject("likes");
		if ((jo != null) && (jo.length() > 0)) {
			result = jo.optInt("count");
		}
		return result;
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

	public Profile[] getTo() {
		Profile[] result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("to");
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

	public String getMessage() {
		return jsonObject.optString("message");
	}

	public String getPictureUrl() {
		return jsonObject.optString("picture");
	}

	public Bitmap getPicture(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Bitmap[] result = new Bitmap[1];
						result[0] = getPicture();
						try {
							listener.onComplete(result, state);
						} catch (Throwable t) {
							t.printStackTrace();
						}
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
				url = getPictureUrl();
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

	public Bitmap getPicture() {
		return getPicture(null, null);
	}

	public String getLink() {
		return jsonObject.optString("link");
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	public String getCaption() {
		return jsonObject.optString("caption");
	}

	public String getDescription() {
		return jsonObject.optString("description");
	}

	public String getSource() {
		return jsonObject.optString("source");
	}

	public Bitmap getIcon(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Bitmap[] result = new Bitmap[1];
						result[0] = getIcon();
						try {
							listener.onComplete(result, state);
						} catch (Throwable t) {
							t.printStackTrace();
						}
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

	public String getAttribution() {
		return jsonObject.optString("attribution");
	}

	public Date getUpdatedTime() {
		return DateUtils.parse(getUpdatedTimeAsString());
	}

	public String getUpdatedTimeAsString() {
		return jsonObject.optString("updated_time");
	}

	public Date getCreatedTime() {
		return DateUtils.parse(getCreatedTimeAsString());
	}

	public String getCreatedTimeAsString() {
		return jsonObject.optString("created_time");
	}

	public String getType() {
		return jsonObject.optString("type");
	}

	public Comment[] getComments(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Comment[] result = getComments();
						try {
							listener.onComplete(result, state);
						} catch (Throwable t) {
							t.printStackTrace();
						}
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
						try {
							listener.onComplete(result, state);
						} catch (Throwable t) {
							t.printStackTrace();
						}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			Profile[] result = null;
			try {
				JSONObject jo = jsonObject.optJSONObject("likes");
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

}
