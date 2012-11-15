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

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.FriendList;
import com.blackberry.facebook.inf.Profile;

public class FacebookFriendList extends FacebookObject implements FriendList {

	public FacebookFriendList(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	public Profile[] getMembers() {
		return getMembers(null, null);
	}

	public Profile[] getMembers(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						Profile[] result = getMembers();
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
				JSONObject jo = fb.read(getId() + "/members");
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

	public boolean addMember(String pId) {
		return addMember(pId, null, null);
	}

	public boolean addMember(final String pId, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						boolean[] result = new boolean[1];
						result[0] = addMember(pId);
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

				JSONObject responseObject = fb.write(getId() + "/members/" + pId, requestObject, true);
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

	public boolean deleteMember(String pId) {
		return deleteMember(pId, null, null);
	}

	public boolean deleteMember(final String pId, final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						boolean[] result = new boolean[1];
						result[0] = deleteMember(pId);
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
				JSONObject responseObject = fb.delete(getId() + "/members/" + pId, true);
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
