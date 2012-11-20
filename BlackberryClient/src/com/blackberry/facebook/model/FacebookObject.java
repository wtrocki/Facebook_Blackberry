/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.model;

import org.json.me.JSONObject;

import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;

public class FacebookObject implements com.blackberry.facebook.model.IObject {

	protected Facebook fb;
	protected JSONObject jsonObject;

	public FacebookObject(Facebook pfb, JSONObject pJsonObject)
			throws FacebookException {
		if ((pfb == null) || (pJsonObject == null)) {
			throw new FacebookException(
					"Unable to create Facebook FacebookObject.");
		}
		fb = pfb;
		jsonObject = pJsonObject;
	}

	public void fetch(boolean force) {
		try {
			if (isStub() || force) {
				String id = getId();
				if ((id != null) && !id.trim().equals("")) {
					JSONObject jo;
					jo = fb.read(id.trim());
					if ((jo != null) && (jo.length() > 0)) {
						jsonObject = jo;
					}
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return jsonObject.optString("id");
	}

	public boolean isStub() {
		return false;
	}

}
