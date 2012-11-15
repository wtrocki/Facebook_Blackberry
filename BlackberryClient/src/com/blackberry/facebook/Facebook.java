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
package com.blackberry.facebook;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.ui.UiApplication;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;
import org.w3c.dom.Document;

import com.blackberry.facebook.dao.FacebookAlbum;
import com.blackberry.facebook.dao.FacebookCheckin;
import com.blackberry.facebook.dao.FacebookComment;
import com.blackberry.facebook.dao.FacebookEducation;
import com.blackberry.facebook.dao.FacebookFriendList;
import com.blackberry.facebook.dao.FacebookInterest;
import com.blackberry.facebook.dao.FacebookLocation;
import com.blackberry.facebook.dao.FacebookPage;
import com.blackberry.facebook.dao.FacebookPhoto;
import com.blackberry.facebook.dao.FacebookPost;
import com.blackberry.facebook.dao.FacebookProfile;
import com.blackberry.facebook.dao.FacebookStatus;
import com.blackberry.facebook.dao.FacebookTag;
import com.blackberry.facebook.dao.FacebookUser;
import com.blackberry.facebook.dao.FacebookVideo;
import com.blackberry.facebook.dao.FacebookWork;
import com.blackberry.facebook.inf.Album;
import com.blackberry.facebook.inf.Checkin;
import com.blackberry.facebook.inf.Comment;
import com.blackberry.facebook.inf.Education;
import com.blackberry.facebook.inf.FriendList;
import com.blackberry.facebook.inf.Interest;
import com.blackberry.facebook.inf.Location;
import com.blackberry.facebook.inf.Page;
import com.blackberry.facebook.inf.Photo;
import com.blackberry.facebook.inf.Post;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.Status;
import com.blackberry.facebook.inf.Tag;
import com.blackberry.facebook.inf.User;
import com.blackberry.facebook.inf.Video;
import com.blackberry.facebook.inf.Work;
import com.blackberry.util.network.HttpClient;
import com.blackberry.util.network.LoggableConnectionFactory;
import com.blackberry.util.ui.BrowserScreen;

public class Facebook {

	protected static final String GRAPH_URL = "https://graph.facebook.com";
	protected static final String ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";
	protected static final String LOADING = "Connecting to Facebook";

	protected HttpClient http;
	protected ConnectionFactory cf;
	protected LoggableConnectionFactory lcf;
	protected int[] preferredTransportTypes = { TransportInfo.TRANSPORT_TCP_WIFI, TransportInfo.TRANSPORT_TCP_CELLULAR, TransportInfo.TRANSPORT_WAP2 };
	protected int[] disallowedTransportTypes = { TransportInfo.TRANSPORT_BIS_B, TransportInfo.TRANSPORT_MDS, TransportInfo.TRANSPORT_WAP };

	protected ApplicationSettings appSettings;
	protected String accessToken;
	protected Object ACCESS_TOKEN_LOCK = new Object();

	protected String id = "";
	protected String pwd = "";
	protected boolean autoMode = false;

	public static class Permissions {

		// FacebookUser Data Permissions
		public static final String USER_ABOUT_ME = "user_about_me";
		public static final String USER_ACTIVITIES = "user_activities";
		public static final String USER_BIRTHDAY = "user_birthday";
		public static final String USER_EDUCATION_HISTORY = "user_education_history";
		public static final String USER_EVENTS = "user_events";
		public static final String USER_GROUPS = "user_groups";
		public static final String USER_HOMETOWN = "user_hometown";
		public static final String USER_INTERESTS = "user_interests";
		public static final String USER_LIKES = "user_likes";
		public static final String USER_LOCATION = "user_location";
		public static final String USER_NOTES = "user_notes";
		public static final String USER_ONLINE_PRESENCE = "user_online_presence";
		public static final String USER_PHOTO_VIDEO_TAGS = "user_photo_video_tags";
		public static final String USER_PHOTOS = "user_photos";
		public static final String USER_RELATIONSHIPS = "user_relationships";
		public static final String USER_RELATIONSHIP_DETAILS = "user_relationship_details";
		public static final String USER_RELIGION_POLITICS = "user_religion_politics";
		public static final String USER_STATUS = "user_status";
		public static final String USER_VIDEOS = "user_videos";
		public static final String USER_WEBSITE = "user_website";
		public static final String USER_WORK_HISTORY = "user_work_history";
		public static final String EMAIL = "email";
		public static final String READ_FRIENDLISTS = "read_friendlists";
		public static final String READ_INSIGHTS = "read_insights";
		public static final String READ_MAILBOX = "read_mailbox";
		public static final String READ_REQUESTS = "read_requests";
		public static final String READ_STREAM = "read_stream";
		public static final String XMPP_LOGIN = "xmpp_login";
		public static final String ADS_MANAGEMENT = "ads_management";
		public static final String USER_CHECKINS = "user_checkins";

		// Friends Data Permissions
		public static final String FRIENDS_ABOUT_ME = "friends_about_me";
		public static final String FRIENDS_ACTIVITIES = "friends_activities";
		public static final String FRIENDS_BIRTHDAY = "friends_birthday";
		public static final String FRIENDS_EDUCATION_HISTORY = "friends_education_history";
		public static final String FRIENDS_EVENTS = "friends_events";
		public static final String FRIENDS_GROUPS = "friends_groups";
		public static final String FRIENDS_HOMETOWN = "friends_hometown";
		public static final String FRIENDS_INTERESTS = "friends_interests";
		public static final String FRIENDS_LIKES = "friends_likes";
		public static final String FRIENDS_LOCATION = "friends_location";
		public static final String FRIENDS_NOTES = "friends_notes";
		public static final String FRIENDS_ONLINE_PRESENCE = "friends_online_presence";
		public static final String FRIENDS_PHOTO_VIDEO_TAGS = "friends_photo_video_tags";
		public static final String FRIENDS_PHOTOS = "friends_photos";
		public static final String FRIENDS_RELATIONSHIPS = "friends_relationships";
		public static final String FRIENDS_RELATIONSHIP_DETAILS = "friends_relationship_details";
		public static final String FRIENDS_RELIGION_POLITICS = "friends_religion_politics";
		public static final String FRIENDS_STATUS = "friends_status";
		public static final String FRIENDS_VIDEOS = "friends_videos";
		public static final String FRIENDS_WEBSITE = "friends_website";
		public static final String FRIENDS_WORK_HISTORY = "friends_work_history";
		public static final String MANAGE_FRIENDLISTS = "manage_friendlists";
		public static final String FRIENDS_CHECKINS = "friends_checkins";

		// Publishing Permissions
		public static final String PUBLISH_STREAM = "publish_stream";
		public static final String CREATE_EVENT = "create_event";
		public static final String RSVP_EVENT = "rsvp_event";
		// public static final String SMS = "sms";
		public static final String OFFLINE_ACCESS = "offline_access";
		public static final String PUBLISH_CHECKINS = "publish_checkins";

		// Page Permissions
		public static final String MANAGE_PAGES = "manage_pages";

		// Some canned permissions bundles
		public static final String[] USER_DATA_PERMISSIONS = new String[] { USER_ABOUT_ME, USER_ACTIVITIES, USER_BIRTHDAY, USER_EDUCATION_HISTORY, USER_EVENTS, USER_GROUPS, USER_HOMETOWN, USER_INTERESTS, USER_LIKES, USER_LOCATION, USER_NOTES, USER_ONLINE_PRESENCE, USER_PHOTO_VIDEO_TAGS, USER_PHOTOS, USER_RELATIONSHIPS, USER_RELATIONSHIP_DETAILS, USER_RELIGION_POLITICS, USER_STATUS, USER_VIDEOS, USER_WEBSITE, USER_WORK_HISTORY, EMAIL, READ_FRIENDLISTS, READ_INSIGHTS, READ_MAILBOX, READ_REQUESTS, READ_STREAM, XMPP_LOGIN, ADS_MANAGEMENT, USER_CHECKINS };
		public static final String[] FRIENDS_DATA_PERMISSIONS = new String[] { FRIENDS_ABOUT_ME, FRIENDS_ACTIVITIES, FRIENDS_BIRTHDAY, FRIENDS_EDUCATION_HISTORY, FRIENDS_EVENTS, FRIENDS_GROUPS, FRIENDS_HOMETOWN, FRIENDS_INTERESTS, FRIENDS_LIKES, FRIENDS_LOCATION, FRIENDS_NOTES, FRIENDS_ONLINE_PRESENCE, FRIENDS_PHOTO_VIDEO_TAGS, FRIENDS_PHOTOS, FRIENDS_RELATIONSHIPS, FRIENDS_RELATIONSHIP_DETAILS, FRIENDS_RELIGION_POLITICS, FRIENDS_STATUS, FRIENDS_VIDEOS, FRIENDS_WEBSITE, FRIENDS_WORK_HISTORY, MANAGE_FRIENDLISTS, FRIENDS_CHECKINS };
		// public static final String[] PUBLISHING_PERMISSIONS = new String[] { PUBLISH_STREAM, CREATE_EVENT, RSVP_EVENT, SMS, OFFLINE_ACCESS, PUBLISH_CHECKINS };
		public static final String[] PUBLISHING_PERMISSIONS = new String[] { PUBLISH_STREAM, CREATE_EVENT, RSVP_EVENT, OFFLINE_ACCESS, PUBLISH_CHECKINS };
		public static final String[] PAGE_PERMISSIONS = new String[] { MANAGE_PAGES };
		// public static final String[] ALL_PERMISSIONS = new String[] { USER_ABOUT_ME, USER_ACTIVITIES, USER_BIRTHDAY, USER_EDUCATION_HISTORY, USER_EVENTS, USER_GROUPS, USER_HOMETOWN, USER_INTERESTS, USER_LIKES, USER_LOCATION, USER_NOTES, USER_ONLINE_PRESENCE, USER_PHOTO_VIDEO_TAGS, USER_PHOTOS, USER_RELATIONSHIPS, USER_RELATIONSHIP_DETAILS, USER_RELIGION_POLITICS, USER_STATUS, USER_VIDEOS, USER_WEBSITE, USER_WORK_HISTORY, EMAIL, READ_FRIENDLISTS, READ_INSIGHTS, READ_MAILBOX, READ_REQUESTS, READ_STREAM, XMPP_LOGIN, ADS_MANAGEMENT, USER_CHECKINS, FRIENDS_ABOUT_ME, FRIENDS_ACTIVITIES, FRIENDS_BIRTHDAY, FRIENDS_EDUCATION_HISTORY, FRIENDS_EVENTS, FRIENDS_GROUPS, FRIENDS_HOMETOWN, FRIENDS_INTERESTS, FRIENDS_LIKES, FRIENDS_LOCATION, FRIENDS_NOTES, FRIENDS_ONLINE_PRESENCE, FRIENDS_PHOTO_VIDEO_TAGS, FRIENDS_PHOTOS, FRIENDS_RELATIONSHIPS, FRIENDS_RELATIONSHIP_DETAILS, FRIENDS_RELIGION_POLITICS, FRIENDS_STATUS, FRIENDS_VIDEOS, FRIENDS_WEBSITE, FRIENDS_WORK_HISTORY, MANAGE_FRIENDLISTS, FRIENDS_CHECKINS, PUBLISH_STREAM, CREATE_EVENT, RSVP_EVENT, SMS, OFFLINE_ACCESS, PUBLISH_CHECKINS, MANAGE_PAGES };
		public static final String[] ALL_PERMISSIONS = new String[] { USER_ABOUT_ME, USER_ACTIVITIES, USER_BIRTHDAY, USER_EDUCATION_HISTORY, USER_EVENTS, USER_GROUPS, USER_HOMETOWN, USER_INTERESTS, USER_LIKES, USER_LOCATION, USER_NOTES, USER_ONLINE_PRESENCE, USER_PHOTO_VIDEO_TAGS, USER_PHOTOS, USER_RELATIONSHIPS, USER_RELATIONSHIP_DETAILS, USER_RELIGION_POLITICS, USER_STATUS, USER_VIDEOS, USER_WEBSITE, USER_WORK_HISTORY, EMAIL, READ_FRIENDLISTS, READ_INSIGHTS, READ_MAILBOX, READ_REQUESTS, READ_STREAM, XMPP_LOGIN, ADS_MANAGEMENT, USER_CHECKINS, FRIENDS_ABOUT_ME, FRIENDS_ACTIVITIES, FRIENDS_BIRTHDAY, FRIENDS_EDUCATION_HISTORY, FRIENDS_EVENTS, FRIENDS_GROUPS, FRIENDS_HOMETOWN, FRIENDS_INTERESTS, FRIENDS_LIKES, FRIENDS_LOCATION, FRIENDS_NOTES, FRIENDS_ONLINE_PRESENCE, FRIENDS_PHOTO_VIDEO_TAGS, FRIENDS_PHOTOS, FRIENDS_RELATIONSHIPS, FRIENDS_RELATIONSHIP_DETAILS, FRIENDS_RELIGION_POLITICS, FRIENDS_STATUS, FRIENDS_VIDEOS, FRIENDS_WEBSITE, FRIENDS_WORK_HISTORY, MANAGE_FRIENDLISTS, FRIENDS_CHECKINS, PUBLISH_STREAM, CREATE_EVENT, RSVP_EVENT, OFFLINE_ACCESS, PUBLISH_CHECKINS, MANAGE_PAGES };

	}

	public static class PictureTypes {
		public static final int SMALL = 1;
		public static final int NORMAL = 2;
		public static final int LARGE = 3;
		public static final int SQUARE = 4;
		public static final int THUMBNAIL = 5;
		public static final int ALBUM = 6;
	}

	public static class PostTypes {
		public static final String PHOTO = "photo";
		public static final String VIDEO = "video";
		public static final String STATUS = "status";
		public static final String LINK = "link";
		public static final String SWF = "swf";
	}

	public static class ProfileTypes {
		public static final int USER = 1;
		public static final int PAGE = 2;
	}

	public static Facebook getInstance(ApplicationSettings pAppSettings) {
		return new Facebook(pAppSettings);
	}

	protected Facebook(ApplicationSettings pAppSettings) {
		appSettings = pAppSettings;
		cf = new ConnectionFactory();
		cf.setPreferredTransportTypes(preferredTransportTypes);
		cf.setDisallowedTransportTypes(disallowedTransportTypes);
		lcf = new LoggableConnectionFactory();
		lcf.setPreferredTransportTypes(preferredTransportTypes);
		lcf.setDisallowedTransportTypes(disallowedTransportTypes);

		http = new HttpClient(cf);
	}

	public void setAutoMode(boolean pAutoMode, String pId, String pPwd) {
		autoMode = pAutoMode;
		id = pId;
		pwd = pPwd;
	}

	public boolean isAutoMode() {
		return autoMode;
	}

	public void setPermissions(String[] pPerms) {
		appSettings.setPermissions(pPerms);
		setAccessToken(null);
	}

	public String[] getPermissions() {
		return appSettings.getPermissions();
	}

	public void addPermission(String pPerm) {
		String[] oldPerms = getPermissions();
		if ((oldPerms != null) && (oldPerms.length > 0)) {
			String[] newPerms = new String[oldPerms.length + 1];
			for (int i = 0; i < oldPerms.length; i++) {
				newPerms[i] = oldPerms[i];
			}
			newPerms[oldPerms.length] = pPerm;
			setPermissions(newPerms);
		} else {
			String[] newPerms = new String[] { pPerm };
			setPermissions(newPerms);
		}
	}

	public StringBuffer checkResponse(StringBuffer res) throws FacebookException {
		StringBuffer result = null;
		try {
			if ((res != null) && (res.length() > 0) && (res.length() < 500)) {
				if ((res.charAt(0) == '{') && (res.charAt(res.length() - 1) == '}')) {
					JSONObject jo = new JSONObject(new JSONTokener(res.toString()));
					if ((jo != null) && jo.has("error")) {
						JSONObject error = jo.getJSONObject("error");
						String errorType = error.optString("type");
						String errorMessage = error.optString("message");
						if ((errorType != null) && errorType.trim().equals("OAuthException")) {
							throw new OAuthException(errorMessage);
						} else {
							throw new UnknownException(errorMessage);
						}
					}
				}
			}
			result = res;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject read(String path) throws FacebookException {
		return read(path, null, true);
	}

	public JSONObject read(String path, Hashtable params, boolean retry) throws FacebookException {
		if (!hasAccessToken()) {
			refreshAccessToken(false);
		}

		JSONObject result = null;

		Hashtable args = new Hashtable();
		args.put("access_token", accessToken);
		args.put("format", "JSON");

		if ((params != null) && (params.size() > 0)) {
			Enumeration paramNamesEnum = params.keys();
			while (paramNamesEnum.hasMoreElements()) {
				String paramName = (String) paramNamesEnum.nextElement();
				String paramValue = (String) params.get(paramName);
				args.put(paramName, paramValue);
			}
		}

		try {
			StringBuffer responseBuffer = checkResponse(http.doGet(GRAPH_URL + '/' + path, args));

			if ((responseBuffer == null) || (responseBuffer.length() <= 0)) {
				result = null;
			}

			result = new JSONObject(new JSONTokener(responseBuffer.toString()));

		} catch (OAuthException e) {
			if (retry) {
				refreshAccessToken(true);
				result = read(path, params, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FacebookException(e.getMessage());

		} catch (Throwable t) {
			t.printStackTrace();
			throw new FacebookException(t.getMessage());
		}
		return result;
	}

	public byte[] readRaw(String path, boolean relative) throws FacebookException {
		return readRaw(path, null, relative, true);
	}

	public byte[] readRaw(String path, Hashtable params, boolean relative, boolean retry) throws FacebookException {
		if (!hasAccessToken()) {
			refreshAccessToken(false);
		}

		byte[] result = null;

		Hashtable args = new Hashtable();
		args.put("access_token", accessToken);

		if ((params != null) && (params.size() > 0)) {
			Enumeration paramNamesEnum = params.keys();
			while (paramNamesEnum.hasMoreElements()) {
				String paramName = (String) paramNamesEnum.nextElement();
				String paramValue = (String) params.get(paramName);
				args.put(paramName, paramValue);
			}
		}

		try {
			StringBuffer responseBuffer;
			if (relative) {
				responseBuffer = checkResponse(http.doGet(GRAPH_URL + '/' + path, args));
			} else {
				responseBuffer = checkResponse(http.doGet(path, args));
			}

			if ((responseBuffer == null) || (responseBuffer.length() <= 0)) {
				throw new FacebookException("Empty Response");
			}

			result = responseBuffer.toString().getBytes();

		} catch (OAuthException e) {
			if (retry) {
				refreshAccessToken(true);
				result = readRaw(path, params, relative, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FacebookException(e.getMessage());

		} catch (Throwable t) {
			t.printStackTrace();
			throw new FacebookException(t.getMessage());
		}
		return result;
	}

	public JSONObject write(String path, JSONObject object, boolean retry) throws FacebookException {
		if (!hasAccessToken()) {
			refreshAccessToken(false);
		}

		JSONObject result = null;

		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");

		try {
			JSONObject jo = object;
			Enumeration keysEnum = jo.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				Object val = jo.get(key);
				if (!(val instanceof JSONObject)) {
					data.put(key, val.toString());
				}
			}

			StringBuffer responseBuffer = checkResponse(http.doPost(GRAPH_URL + '/' + path, data));

			if ((responseBuffer == null) || (responseBuffer.length() <= 0)) {
				return null;
			}

			result = new JSONObject(new JSONTokener(responseBuffer.toString()));

		} catch (OAuthException e) {
			if (retry) {
				refreshAccessToken(true);
				result = write(path, object, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FacebookException(e.getMessage());

		} catch (Throwable t) {
			t.printStackTrace();
			throw new FacebookException(t.getMessage());
		}
		return result;
	}

	public JSONObject write(String path, JSONObject object, String contentType, byte[] payload, boolean retry) throws FacebookException {
		if (!hasAccessToken()) {
			refreshAccessToken(false);
		}

		JSONObject result = null;

		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");

		try {
			JSONObject jo = object;
			Enumeration keysEnum = jo.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				Object val = jo.get(key);
				if (!(val instanceof JSONObject)) {
					data.put(key, val.toString());
				}
			}

			StringBuffer responseBuffer = checkResponse(http.doPostMultipart(GRAPH_URL + '/' + path, data, "name", "filename", contentType, payload));

			if ((responseBuffer == null) || (responseBuffer.length() <= 0)) {
				return null;
			}

			result = new JSONObject(new JSONTokener(responseBuffer.toString()));

		} catch (OAuthException e) {
			if (retry) {
				refreshAccessToken(true);
				result = write(path, object, contentType, payload, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FacebookException(e.getMessage());

		} catch (Throwable t) {
			t.printStackTrace();
			throw new FacebookException(t.getMessage());
		}
		return result;
	}

	public JSONObject delete(String path, boolean retry) throws FacebookException {
		if (!hasAccessToken()) {
			refreshAccessToken(false);
		}

		JSONObject result = null;

		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");
		data.put("method", "delete");

		try {
			StringBuffer responseBuffer = checkResponse(http.doPost(GRAPH_URL + '/' + path, data));

			if ((responseBuffer == null) || (responseBuffer.length() <= 0)) {
				return null;
			}

			result = new JSONObject(new JSONTokener(responseBuffer.toString()));

		} catch (OAuthException e) {
			if (retry) {
				refreshAccessToken(true);
				result = delete(path, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FacebookException(e.getMessage());

		} catch (Throwable t) {
			t.printStackTrace();
			throw new FacebookException(t.getMessage());
		}
		return result;
	}

	public User getCurrentUser(final AsyncCallback listener) throws FacebookException {
		return getUser("me", listener, null);
	}

	public User getCurrentUser() throws FacebookException {
		return getUser("me", null, null);
	}

	public User getUser(final Profile pProfile, final AsyncCallback listener) throws FacebookException {
		return getUser(pProfile.getId(), listener, null);
	}

	public User getUser(Profile pProfile) throws FacebookException {
		return getUser(pProfile.getId());
	}

	public User getUser(String pId) throws FacebookException {
		return getUser(pId, null, null);
	}

	public User getUser(final String pId, final AsyncCallback listener, final java.lang.Object state) throws FacebookException {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						User[] result = new User[1];
						result[0] = getUser(pId);
						listener.onComplete(result, null);

					} catch (Exception e) {
						listener.onException(e, null);
					}
				}
			}.start();
			return null;

		} else {
			if ((pId == null) || pId.trim().equals("")) {
				return null;
			}

			User result = null;

			JSONObject jsonObject = read(pId);
			if ((jsonObject == null) || (jsonObject.length() <= 0)) {
				result = null;
			} else {
				result = getUser(jsonObject);
			}
			return result;
		}
	}

	public User getUser(JSONObject jo) throws FacebookException {
		return new FacebookUser(this, jo);
	}

	public Tag getTag(JSONObject jo) throws FacebookException {
		return new FacebookTag(this, jo);
	}

	public FriendList getFriendList(JSONObject jo) throws FacebookException {
		return new FacebookFriendList(this, jo);
	}

	public Comment getComment(JSONObject jo) throws FacebookException {
		return new FacebookComment(this, jo);
	}

	public Album getAlbum(JSONObject jo) throws FacebookException {
		return new FacebookAlbum(this, jo);
	}

	public Location getLocation(JSONObject jo) throws FacebookException {
		return new FacebookLocation(this, jo);
	}

	public Work getWork(JSONObject jo) throws FacebookException {
		return new FacebookWork(this, jo);
	}

	public Education getEducation(JSONObject jo) throws FacebookException {
		return new FacebookEducation(this, jo);
	}

	public Profile getProfile(JSONObject jo) throws FacebookException {
		return new FacebookProfile(this, jo);
	}

	public Interest getInterest(JSONObject jo) throws FacebookException {
		return new FacebookInterest(this, jo);
	}

	public Video getVideo(JSONObject jo) throws FacebookException {
		return new FacebookVideo(this, jo);
	}

	public Page getPage(JSONObject jo) throws FacebookException {
		return new FacebookPage(this, jo);
	}

	public void getPage(final Profile pProfile, final AsyncCallback listener) throws FacebookException {
		getPage(pProfile.getId(), listener, null);
	}

	public Page getPage(Profile pProfile) throws FacebookException {
		return getPage(pProfile.getId());
	}

	public Page getPage(String pId) throws FacebookException {
		return getPage(pId, null, null);
	}

	public Page getPage(final String pId, final AsyncCallback listener, final java.lang.Object state) throws FacebookException {

		if (listener != null) {
			new Thread() {
				public void run() {
					try {
						Page[] result = new Page[1];
						result[0] = getPage(pId);
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

			if ((pId == null) || pId.trim().equals("")) {
				return null;
			}

			Page result = null;

			JSONObject jsonObject = read(pId);
			if ((jsonObject == null) || (jsonObject.length() <= 0)) {
				result = null;
			} else {
				result = getPage(jsonObject);
			}
			return result;
		}

	}

	public Checkin getCheckin(JSONObject jo) throws FacebookException {
		return new FacebookCheckin(this, jo);
	}

	public Status getStatus(JSONObject jo) throws FacebookException {
		return new FacebookStatus(this, jo);
	}

	public Photo getPhoto(JSONObject jo) throws FacebookException {
		return new FacebookPhoto(this, jo);
	}

	public Post getPost(JSONObject jo) throws FacebookException {
		return new FacebookPost(this, jo);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String pAccessToken) {
		accessToken = pAccessToken;
	}

	public boolean hasAccessToken() {
		String at = getAccessToken();
		if ((at == null) || at.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isAccessTokenValid() {
		boolean result = false;
		if (!hasAccessToken()) {
			result = false;
		} else {
			try {
				JSONObject jsonObject = read("me", null, false);
				if ((jsonObject == null) || (jsonObject.length() <= 0)) {
					result = false;
				} else {
					result = true;
				}

			} catch (FacebookException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	public void refreshAccessToken(boolean force) throws FacebookException {
		synchronized (ACCESS_TOKEN_LOCK) {
			if (force || !isAccessTokenValid()) {
				setAccessToken(null);
				if (net.rim.device.api.system.Application.isEventDispatchThread()) {
					UiApplication.getUiApplication().pushModalScreen(new LoginScreen());
				} else {
					UiApplication.getApplication().invokeAndWait(new Runnable() {
						public void run() {
							UiApplication.getUiApplication().pushModalScreen(new LoginScreen());
						}
					});
				}

				if (!hasAccessToken()) {
					throw new FacebookException("Unable to refresh the Access Token");
				}
			}
		}
	}

	protected class LoginScreen extends BrowserScreen {

		protected LoginScreen() {
			super("https://www.facebook.com/dialog/oauth?scope=" + appSettings.getPermissionsString() + "&redirect_uri=" + appSettings.getNextUrl() + "&display=wap&client_id=" + appSettings.getApplicationId() + "&response_type=token", lcf, LOADING);
		}

		protected boolean hasAccessToken(String pUrl) {
			boolean result = false;
			String token = getAccessTokenFromUrl(pUrl);
			if ((token == null) || token.trim().equals("")) {
				result = false;
			} else {
				setAccessToken(token);
				dismiss();
				result = true;
			}
			return result;
		}

		protected boolean shouldFetchContent(BrowserFieldRequest request) {
			return !hasAccessToken(request.getURL());
		}

		protected boolean shouldShowContent(BrowserField pbf, Document pdoc) {
			return !hasAccessToken(pdoc.getDocumentURI());
		}

		protected boolean postProcessing(BrowserField pbf, Document pdoc) {
			if (isAutoMode()) {
				if ((pdoc != null)) {
					String jsFillId = "document.forms['login_form'].elements['email'].value = '" + id + "';";
					String jsFillPwd = "document.forms['login_form'].elements['pass'].value = '" + pwd + "';";
					String jsLogin = "setTimeout(\"var evt_l = document.createEvent('MouseEvents'); evt_l.initEvent('click', true, true); document.forms['login_form'].elements['login'].dispatchEvent(evt_l);\",3000);";
					String jsGrant = "setTimeout(\"var evt_g = document.createEvent('MouseEvents'); evt_g.initEvent('click', true, true); document.getElementsByName('grant_clicked')[0].dispatchEvent(evt_g);\",3000);";

					if ((pdoc.getElementById("login_form") != null)) {
						pbf.executeScript(jsFillId);
						pbf.executeScript(jsFillPwd);
						pbf.executeScript(jsLogin);

					} else if ((pdoc.getElementById("uiserver_form") != null)) {
						pbf.executeScript(jsGrant);
					} else {
					}
				} else {
				}
			}
			return true;
		}

		protected String getAccessTokenFromUrl(String url) {
			String code = null;
			String at = null;

			if ((url != null) && !url.trim().equals("")) {
				int startIndex = url.indexOf("#access_token=");
				if (startIndex > -1) {
					startIndex++;
					int stopIndex = url.length();
					if (url.indexOf('&', startIndex) > -1) {
						stopIndex = url.indexOf('&', startIndex);
					} else if (url.indexOf(';', startIndex) > -1) {
						stopIndex = url.indexOf(';', startIndex);
					}
					at = url.substring(url.indexOf('=', startIndex) + 1, stopIndex);

				} else {
					startIndex = url.indexOf("?code=");
					if (startIndex > -1) {
						startIndex++;
						int stopIndex = url.length();
						if (url.indexOf('&', startIndex) > -1) {
							stopIndex = url.indexOf('&', startIndex);
						} else if (url.indexOf(';', startIndex) > -1) {
							stopIndex = url.indexOf(';', startIndex);
						}
						code = url.substring(url.indexOf('=', startIndex) + 1, stopIndex);
						at = getAccessTokenFromCode(code);
					}
				}
			}

			return at;
		}

		protected String getAccessTokenFromCode(String pCode) {
			String at = null;

			if ((pCode == null) || pCode.trim().equals("")) {
				return null;
			}

			Hashtable args = new Hashtable();
			args.put("client_id", appSettings.applicationId);
			args.put("client_secret", appSettings.applicationSecret);
			args.put("redirect_uri", appSettings.nextUrl);
			args.put("code", pCode);

			try {
				StringBuffer responseBuffer = http.doGet(ACCESS_TOKEN_URL, args);

				if ((responseBuffer == null) || (responseBuffer.length() <= 0)) {
					at = null;
				} else {
					String temp_at = responseBuffer.toString().trim();
					if (!temp_at.startsWith("access_token=")) {
						at = null;
					} else {
						at = temp_at.substring(13);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();

			} catch (Throwable t) {
				t.printStackTrace();
			}

			return at;
		}
	}

	public boolean logout() {
		return logout(true);
	}

	public boolean logout(boolean clearAccessToken) {
		synchronized (ACCESS_TOKEN_LOCK) {
			String at = getAccessToken();

			if ((at != null) && !at.equals("")) {
				final LogoutScreen logoutScreen = new LogoutScreen(at);

				if (net.rim.device.api.system.Application.isEventDispatchThread()) {
					UiApplication.getUiApplication().pushModalScreen(logoutScreen);
				} else {
					UiApplication.getApplication().invokeAndWait(new Runnable() {
						public void run() {
							UiApplication.getUiApplication().pushModalScreen(logoutScreen);
						}
					});
				}

				if (clearAccessToken) {
					setAccessToken(null);
				}

				return logoutScreen.loggedOut;

			} else {
				if (clearAccessToken) {
					setAccessToken(null);
				}
				return true;
			}
		}
	}

	protected class LogoutScreen extends BrowserScreen {

		public boolean loggedOut = false;

		protected LogoutScreen(String pAccessToken) {
			super("https://m.facebook.com/logout.php?next=" + appSettings.getNextUrl() + "&access_token=" + pAccessToken, lcf, LOADING);
		}

		protected boolean hasLogoutStatus(String pUrl) {
			boolean result = false;
			if (!checkLogoutStatusFromUrl(pUrl)) {
				result = false;
			} else {
				loggedOut = true;
				dismiss();
				result = true;
			}
			return result;
		}

		protected boolean shouldFetchContent(BrowserFieldRequest request) {
			return !hasLogoutStatus(request.getURL());
		}

		protected boolean shouldShowContent(BrowserField pbf, Document pdoc) {
			return !hasLogoutStatus(pdoc.getDocumentURI());
		}

		protected boolean checkLogoutStatusFromUrl(String url) {
			boolean result = false;
			if ((url != null) && !url.trim().equals("")) {
				if ((url.indexOf("login_success.html") > -1) && (url.indexOf("logout.php") == -1)) {
					result = true;
				} else {
					result = false;
				}
			}
			return result;
		}

	}

}
