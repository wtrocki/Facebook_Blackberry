/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
/*******************************************************************************
 *******************************************************************************/
package com.blackberry.facebook.objects;

import org.json.me.JSONObject;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;

public class FacebookProfile extends FacebookObject implements Profile {

	public FacebookProfile(Facebook pfb, JSONObject pJsonObject)
			throws FacebookException {
		super(pfb, pJsonObject);
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	protected int getProfileType() {
		fetch(false);
		if (jsonObject.has("first_name") || jsonObject.has("last_name")
				|| jsonObject.has("birthday") || jsonObject.has("gender")
				|| jsonObject.has("relationship_status")
				|| jsonObject.has("significant_other")
				|| jsonObject.has("hometown") || jsonObject.has("bio")
				|| jsonObject.has("education")
				|| jsonObject.has("interested_in")
				|| jsonObject.has("meeting_for")) {
			return Facebook.ProfileTypes.USER;
		} else {
			return Facebook.ProfileTypes.PAGE;
		}
	}

	public com.blackberry.facebook.objects.Object toObject() {
		return toObject(null, null);
	}

	public com.blackberry.facebook.objects.Object toObject(
			final AsyncCallback listener, final java.lang.Object state) {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						com.blackberry.facebook.objects.Object[] result = new com.blackberry.facebook.objects.Object[1];
						result[0] = toObject();
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
			com.blackberry.facebook.objects.Object result = null;
			try {
				int profileType = getProfileType();
				if (profileType == Facebook.ProfileTypes.USER) {
					result = fb.getUser(jsonObject);
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
