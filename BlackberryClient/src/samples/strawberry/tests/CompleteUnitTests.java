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
package samples.strawberry.tests;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.Dialog;

import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Album;
import com.blackberry.facebook.inf.Checkin;
import com.blackberry.facebook.inf.Photo;
import com.blackberry.facebook.inf.Post;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.Status;
import com.blackberry.facebook.inf.User;
import com.blackberry.util.log.Logger;
import com.blackberry.util.unittest.UnitTests;
import com.blackberry.util.unittest.jmunit.Assertion;
import com.blackberry.util.unittest.jmunit.AssertionFailedException;

public class CompleteUnitTests implements UnitTests {

	protected Logger log = Logger.getLogger(getClass());
	protected Facebook fb;
	protected String id;
	protected String pwd;
	protected BrowserField bf;

	public CompleteUnitTests(Facebook pfb, String pId, String pPwd) {
		fb = pfb;
		id = pId;
		pwd = pPwd;
		bf = new BrowserField();
	}

	public void runTests() throws AssertionFailedException {
		Assertion.reset();
		fb.setAutoMode(true, id, pwd);
		testLogin();
		testUserProperties();
		testUserConnections();
		testLogout();
	}

	protected void logCheckPoint(boolean pause, String pMsg) {
		log.debug("###############################################################################");
		log.debug("###########  Check Point = " + pMsg);
		log.debug("###########  Access Token = " + fb.getAccessToken());
		log.debug("###########  FB Cookies = " + bf.getCookieManager().getCookie("http://www.facebook.com"));
		log.debug("###########  Permissions = " + fb.getPermissions().length);
		log.debug("###############################################################################");
		if (pause) {
			synchronized (net.rim.device.api.system.Application.getEventLock()) {
				String token = fb.getAccessToken();
				if (token == null) {
					token = "";
				}
				Dialog.inform(pMsg + ", Token=" + token.length() + ", Perms=" + fb.getPermissions().length);
			}
		}
	}

	protected void testLogin() throws AssertionFailedException {
		String testName = "testLogin()";
		int i = 0;
		String[] perms = Facebook.Permissions.FRIENDS_DATA_PERMISSIONS;
		String tempAT;
		boolean pause = false;

		Assertion.addSeparator(testName);

		// Login Flow 1
		addPermission(perms[i++]);
		logoutAndClearToken();
		logCheckPoint(pause, "Login Flow 1 begins:  Login > Allow");
		Assertion.assertTrue("Login Flow 1", canGetUser());
		logCheckPoint(pause, "Login Flow 1 finished");

		// Login Flow 2
		logoutAndClearToken();
		logCheckPoint(pause, "Login Flow 2 begins:  Login > Null");
		Assertion.assertTrue("Login Flow 2", canGetUser());
		logCheckPoint(pause, "Login Flow 2 finished");

		// Login Flow 3
		addPermission(perms[i++]);
		clearToken();
		logCheckPoint(pause, "Login Flow 3 begins:  Null > Allow");
		Assertion.assertTrue("Login Flow 3", canGetUser());
		logCheckPoint(pause, "Login Flow 3 finished");

		// Login Flow 4
		clearToken();
		logCheckPoint(pause, "Login Flow 4 begins:  Null > Null");
		Assertion.assertTrue("Login Flow 4", canGetUser());
		logCheckPoint(pause, "Login Flow 4 finished");

		// Login Flow 5
		addPermission(perms[i++]);
		logoutAndClearToken();
		setWrongToken();
		logCheckPoint(pause, "Login Flow 5 begins:  Login > Allow");
		Assertion.assertTrue("Login Flow 5", canGetUser());
		logCheckPoint(pause, "Login Flow 5 finished");

		// Login Flow 6
		logoutAndClearToken();
		setWrongToken();
		logCheckPoint(pause, "Login Flow 6 begins:  Login > Null");
		Assertion.assertTrue("Login Flow 6", canGetUser());
		logCheckPoint(pause, "Login Flow 6 finished");

		// Login Flow 7
		addPermission(perms[i++]);
		setWrongToken();
		logCheckPoint(pause, "Login Flow 7 begins:  Null > Allow");
		Assertion.assertTrue("Login Flow 7", canGetUser());
		logCheckPoint(pause, "Login Flow 7 finished");

		// Login Flow 8
		setWrongToken();
		logCheckPoint(pause, "Login Flow 8 begins:  Null > Null");
		Assertion.assertTrue("Login Flow 8", canGetUser());
		logCheckPoint(pause, "Login Flow 8 finished");

	}

	protected void testUserProperties() throws AssertionFailedException {
		String testName = "testUserProperties()";
		try {
			Assertion.addSeparator(testName);
			User user = fb.getCurrentUser();
			Assertion.assertNotNull("User", user);
			if (user != null) {
				Assertion.assertNotNull("id", user.getId());
				Assertion.assertNotNull("name", user.getName());
				Assertion.assertNotNull("updated_time", user.getUpdatedTime());
			} else {
				Assertion.fail(testName);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			Assertion.fail(testName);

		} finally {
		}
	}

	protected void testUserConnections() throws AssertionFailedException {
		String testName = "testUserConnections()";
		try {
			Assertion.addSeparator(testName);
			User user = fb.getCurrentUser();
			Assertion.assertNotNull("User", user);
			if (user != null) {

				Bitmap picture = user.getPicture(Facebook.PictureTypes.SMALL);
				Assertion.assertNotNull("picture", picture);
				if (picture != null) {
					Assertion.assertGreaterThan("picture.width", picture.getWidth(), 0);
					Assertion.assertGreaterThan("picture.height", picture.getHeight(), 0);
				}

				Profile[] friends = user.getFriends();
				Assertion.assertNotNull("friends", friends);
				if (friends != null) {
					Assertion.assertGreaterThan("friends.length", friends.length, 0);
					Assertion.assertNotNull("friends[0].id", friends[0].getId());
				}

				Album[] albums = user.getAlbums();
				Assertion.assertNotNull("albums", albums);
				if (albums != null) {
					Assertion.assertGreaterThan("albums.length", albums.length, 0);
					Assertion.assertNotNull("albums[0].id", albums[0].getId());
				}

				Checkin[] checkins = user.getCheckins();
				Assertion.assertNotNull("checkins", checkins);
				if (checkins != null) {
					Assertion.assertGreaterThan("checkins.length", checkins.length, 0);
					Assertion.assertNotNull("checkins[0].id", checkins[0].getId());
				}

				Post[] feed = user.getFeed();
				Assertion.assertNotNull("feed", feed);
				if (feed != null) {
					Assertion.assertGreaterThan("feed.length", feed.length, 0);
					Assertion.assertNotNull("feed[0].id", feed[0].getId());
				}

				Post[] home = user.getHome();
				Assertion.assertNotNull("home", home);
				if (home != null) {
					Assertion.assertGreaterThan("home.length", home.length, 0);
					Assertion.assertNotNull("home[0].id", home[0].getId());
				}

				Photo[] photos = user.getPhotos();
				Assertion.assertNotNull("photos", photos);
				if (photos != null) {
					Assertion.assertGreaterThan("photos.length", photos.length, 0);
					Assertion.assertNotNull("photos[0].id", photos[0].getId());
				}

				Post[] posts = user.getPosts();
				Assertion.assertNotNull("posts", posts);
				if (posts != null) {
					Assertion.assertGreaterThan("posts.length", posts.length, 0);
					Assertion.assertNotNull("posts[0].id", posts[0].getId());
				}

				Status[] statuses = user.getStatuses();
				Assertion.assertNotNull("statuses", statuses);
				if (statuses != null) {
					Assertion.assertGreaterThan("statuses.length", statuses.length, 0);
					Assertion.assertNotNull("statuses[0].id", statuses[0].getId());
				}

			} else {
				Assertion.fail(testName);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			Assertion.fail(testName);

		} finally {
		}
	}

	protected void testLogout() throws AssertionFailedException {
		String testName = "testLogout()";
		try {
			Assertion.addSeparator(testName);
			fb.getCurrentUser();
			fb.logout();
			Assertion.assertNull("Access Token", fb.getAccessToken());
			Assertion.assertFalse("Validity", fb.isAccessTokenValid());

		} catch (Throwable e) {
			e.printStackTrace();
			Assertion.fail(testName);

		} finally {
		}
	}

	// Helper Methods

	protected void addPermission(String pPerm) throws AssertionFailedException {
		fb.addPermission(pPerm);
	}

	protected void logoutAndClearToken() throws AssertionFailedException {
		fb.logout(true);
	}

	protected void logoutAndKeepToken() throws AssertionFailedException {
		fb.logout(false);
	}

	protected void clearToken() throws AssertionFailedException {
		fb.setAccessToken(null);
	}

	protected void setWrongToken() throws AssertionFailedException {
		fb.setAccessToken("helloworld");
	}

	protected boolean canGetUser() throws AssertionFailedException {
		boolean result = false;
		try {
			User user = fb.getCurrentUser();
			if ((user != null) && (user.getId() != null) && !user.getId().equals("") && (user.getName() != null) && !user.getName().equals("")) {
				result = true;
			} else {
				result = false;
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

}
