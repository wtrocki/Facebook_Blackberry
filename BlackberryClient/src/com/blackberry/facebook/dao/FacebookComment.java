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

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Comment;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.util.date.DateUtils;
import com.blackberry.util.json.JSONArray;
import com.blackberry.util.json.JSONException;
import com.blackberry.util.json.JSONObject;

public class FacebookComment extends FacebookObject implements Comment {

	public FacebookComment(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public Date getCreatedTime() {
		return DateUtils.parse(getCreatedTimeAsString());
	}

	public String getCreatedTimeAsString() {
		return jsonObject.optString("created_time");
	}

	public String getMessage() {
		return jsonObject.optString("message");
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

	public int getLikes() {
		return jsonObject.optInt("likes");
	}

	public Profile[] getLikedUsers() {
		return getLikedUsers(null, null);
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

	public boolean like() {
		return like(null, null);
	}

	public boolean like(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						boolean[] result = new boolean[1];
						result[0] = like();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return false;

		} else {
			boolean result = false;
			try {
				JSONObject requestObject = new JSONObject();
				requestObject.put("dummy", "dummy");

				JSONObject responseObject = fb.write(getId() + "/likes", requestObject, true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error")) {
					result = false;
				} else {
					result = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public boolean unlike() {
		return unlike(null, null);
	}

	public boolean unlike(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						boolean[] result = new boolean[1];
						result[0] = unlike();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return false;

		} else {
			boolean result = false;
			try {
				JSONObject responseObject = fb.delete(getId() + "/likes", true);
				if ((responseObject == null) || (responseObject.length() <= 0) || responseObject.has("error")) {
					result = false;
				} else {
					result = true;
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

}
