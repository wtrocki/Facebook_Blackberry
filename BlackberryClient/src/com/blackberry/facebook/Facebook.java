/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
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

import com.blackberry.facebook.model.FacebookUser;
import com.blackberry.facebook.model.IUser;

public class Facebook {

	protected static final String GRAPH_URL = "https://graph.facebook.com";
	protected static final String ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";

	protected HttpClient http;
	protected ConnectionFactory lcf;

	protected ApplicationSettings appSettings;
	protected String accessToken;
	private String accessTokenExpireDate;
	protected Object ACCESS_TOKEN_LOCK = new Object();

	public static Facebook getInstance(ApplicationSettings pAppSettings) {
		return new Facebook(pAppSettings);
	}

	protected Facebook(ApplicationSettings pAppSettings) {
		appSettings = pAppSettings;
		int[] preferredTransportTypes = {TransportInfo.TRANSPORT_TCP_WIFI, TransportInfo.TRANSPORT_TCP_CELLULAR,
				TransportInfo.TRANSPORT_WAP2};
		int[] disallowedTransportTypes = {TransportInfo.TRANSPORT_BIS_B, TransportInfo.TRANSPORT_MDS,
				TransportInfo.TRANSPORT_WAP};
		lcf = new ConnectionFactory();
		lcf.setPreferredTransportTypes(preferredTransportTypes);
		lcf.setDisallowedTransportTypes(disallowedTransportTypes);
		ConnectionFactory cf = new ConnectionFactory();
		cf.setPreferredTransportTypes(preferredTransportTypes);
		cf.setDisallowedTransportTypes(disallowedTransportTypes);
		http = new HttpClient(cf);
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
			String[] newPerms = new String[] {pPerm};
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
			throw new FacebookException(e.getMessage());

		} catch (Throwable t) {
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

	public IUser getCurrentUser(final AsyncCallback listener) throws FacebookException {
		return getUser("me", listener, null);
	}

	public IUser getCurrentUser() throws FacebookException {
		return getUser("me", null, null);
	}

	public IUser getUser(String pId) throws FacebookException {
		return getUser(pId, null, null);
	}

	public IUser getUser(final String pId, final AsyncCallback listener, final java.lang.Object state)
		throws FacebookException {
		if (listener != null) {
			new java.lang.Thread() {
				public void run() {
					try {
						IUser[] result = new IUser[1];
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

			IUser result = null;

			JSONObject jsonObject = read(pId);
			if ((jsonObject == null) || (jsonObject.length() <= 0)) {
				result = null;
			} else {
				result = getUser(jsonObject);
			}
			return result;
		}
	}

	public IUser getUser(JSONObject jo) throws FacebookException {
		return new FacebookUser(this, jo);
	}

	public String getAccessToken() {
		return accessToken;
	}

	protected void setAccessToken(String pAccessToken) {
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
				final LoginScreen loginScreen = new LoginScreen();
				if (net.rim.device.api.system.Application.isEventDispatchThread()) {
					UiApplication.getUiApplication().pushModalScreen(loginScreen);
				} else {
					UiApplication.getApplication().invokeAndWait(new Runnable() {
						public void run() {
							UiApplication.getUiApplication().pushModalScreen(loginScreen);
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
			super("https://www.facebook.com/dialog/oauth?scope=" + appSettings.getPermissionsString()
				+ "&redirect_uri=" + appSettings.getNextUrl() + "&display=wap&client_id="
				+ appSettings.getApplicationId() + "&response_type=token", lcf);
		}

		protected boolean hasAccessToken(String pUrl) {
			boolean result = false;
			String token = getAccessTokenFromUrl(pUrl);
			if ((token == null) || token.trim().equals("")) {
				result = false;
			} else {
				setAccessToken(token);
				setAccessTokenExpireDate(getExpireDate(pUrl));
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

		private String getExpireDate(String tokenUrl) {
			String at = null;
			if ((tokenUrl != null) && !tokenUrl.trim().equals("")) {
				int startIndex = tokenUrl.indexOf("&expires_in=");
				if (startIndex > -1) {
					startIndex++;
					int stopIndex = tokenUrl.length();
					if (tokenUrl.indexOf('&', startIndex) > -1) {
						stopIndex = tokenUrl.indexOf('&', startIndex);
					} else if (tokenUrl.indexOf(';', startIndex) > -1) {
						stopIndex = tokenUrl.indexOf(';', startIndex);
					}
					at = tokenUrl.substring(tokenUrl.indexOf('=', startIndex) + 1, stopIndex);
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

	public String getAccessTokenExpireDate() {
		return accessTokenExpireDate;
	}

	public void setAccessTokenExpireDate(String accessTokenExpireDate) {
		this.accessTokenExpireDate = accessTokenExpireDate;
	}

	protected class LogoutScreen extends BrowserScreen {

		public boolean loggedOut = false;

		protected LogoutScreen(String pAccessToken) {
			super("https://m.facebook.com/logout.php?next=" + appSettings.getNextUrl() + "&access_token="
				+ pAccessToken, lcf);
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
