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

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.util.json.JSONObject;

public class FacebookProfile extends FacebookObject implements Profile {

	public FacebookProfile(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	protected int getProfileType() {
		fetch(false);
		if (jsonObject.has("first_name") || jsonObject.has("last_name") || jsonObject.has("birthday") || jsonObject.has("gender") || jsonObject.has("relationship_status") || jsonObject.has("significant_other") || jsonObject.has("hometown") || jsonObject.has("bio") || jsonObject.has("education") || jsonObject.has("interested_in") || jsonObject.has("meeting_for")) {
			return Facebook.ProfileTypes.USER;
		} else {
			return Facebook.ProfileTypes.PAGE;
		}
	}

	public com.blackberry.facebook.inf.Object toObject() {
		return toObject(null, null);
	}

	public com.blackberry.facebook.inf.Object toObject(final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						com.blackberry.facebook.inf.Object[] result = new com.blackberry.facebook.inf.Object[1];
						result[0] = toObject();
						try {listener.onComplete(result, state);} catch (Throwable t) {t.printStackTrace();}
					} catch (Exception e) {
						listener.onException(e, state);
					}
				}
			}.start();
			return null;

		} else {
			com.blackberry.facebook.inf.Object result = null;
			try {
				int profileType = getProfileType();
				if (profileType == Facebook.ProfileTypes.USER) {
					result = fb.getUser(jsonObject);
				} else if (profileType == Facebook.ProfileTypes.PAGE) {
					result = fb.getPage(jsonObject);
				} else {
					result = null;
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public boolean isStub() {
		return (jsonObject == null) || (jsonObject.length() <= 2);
	}

}
