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
import com.blackberry.facebook.inf.Comment;
import com.blackberry.facebook.inf.Photo;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.util.date.DateUtils;

public class FacebookAlbum extends FacebookObject implements Album {

	public FacebookAlbum(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	// Properties

	public Profile getFrom() {
		Profile result = null;
		try {
			result = fb.getProfile(jsonObject.optJSONObject("from"));
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getName() {
		return jsonObject.optString("name");
	}

	public String getDescription() {
		return jsonObject.optString("description");
	}

	public String getLocation() {
		return jsonObject.optString("location");
	}

	public String getLink() {
		return jsonObject.optString("link");
	}

	public String getPrivacy() {
		return jsonObject.optString("privacy");
	}

	public int getCount() {
		return jsonObject.optInt("count");
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

	public String getType() {
		return jsonObject.optString("type");
	}

	// Connections

	public Photo[] getPhotos() {
		return getPhotos(null, null);
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

	public Comment[] getComments() {
		return getComments(null, null);
	}

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

	public Bitmap getPicture(int pPictureType) {
		return getPicture(pPictureType, null, null);
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
				} else if ((pPictureType == Facebook.PictureTypes.THUMBNAIL)) {
					params.put("type", "thumbnail");
				} else if ((pPictureType == Facebook.PictureTypes.ALBUM)) {
					params.put("type", "album");
				} else {
					params.put("type", "album");
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

	// Publishing

	public String publishPhoto(EncodedImage pPhoto, String pMessage) {
		return publishPhoto(pPhoto, pMessage, null, null);
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

}
