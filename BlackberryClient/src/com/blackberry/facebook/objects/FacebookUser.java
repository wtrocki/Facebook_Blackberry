/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.objects;

import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.blackberry.facebook.AsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;

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

	public String getBirthdayAsString() {
		return jsonObject.optString("birthday");
	}

	public String getEmail() {
		return jsonObject.optString("email");
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

	public String getWebsite() {
		return jsonObject.optString("website");
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

	public Thread[] getInbox() {
		// TODO
		return null;
	}


}