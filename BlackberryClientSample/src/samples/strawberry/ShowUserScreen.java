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
package samples.strawberry;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.User;

public class ShowUserScreen extends MainScreen {

	protected ButtonField tryAgainButton = null;
	protected ButtonField friendsButton = null;
	protected ButtonField statusesButton = null;
	protected ButtonField feedButton = null;
	protected ButtonField homeButton = null;
	protected ButtonField checkinsButton = null;
	protected ButtonField postWallButton = null;
	protected ButtonField uploadPhotosButton = null;
	protected ButtonField checkinButton = null;
	protected VerticalFieldManager vfm1 = null;
	protected VerticalFieldManager vfm2 = null;
	protected VerticalFieldManager vfm3 = null;
	protected Facebook fb;
	protected User user;

	public ShowUserScreen(Facebook pfb) {
		this(pfb, "me");
	}

	public ShowUserScreen(Facebook pfb, final String pUserId) {
		super();
		fb = pfb;

		tryAgainButton = new ButtonField("Try Again") {
			protected boolean invokeAction(int action) {
				delete(vfm1);
				delete(vfm2);
				showUserSync(pUserId);
				return true;
			}
		};

		friendsButton = new ButtonField("Show Friends") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new ShowFriendsScreen(fb, user));
				return true;
			}
		};

		statusesButton = new ButtonField("Show Statuses") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new ShowStatusesScreen(fb, user));
				return true;
			}
		};

		feedButton = new ButtonField("Show Feed") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new ShowFeedScreen(fb, user));
				return true;
			}
		};

		homeButton = new ButtonField("Show Home") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new ShowHomeScreen(fb, user));
				return true;
			}
		};

		checkinsButton = new ButtonField("Show Checkins") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new ShowCheckinsScreen(fb, user));
				return true;
			}
		};

		postWallButton = new ButtonField("Post to Wall") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new PostWallScreen(fb, user));
				return true;
			}
		};

		uploadPhotosButton = new ButtonField("Upload Photos") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new UploadPhotosScreen(fb, user));
				return true;
			}
		};

		checkinButton = new ButtonField("Checkin") {
			protected boolean invokeAction(int action) {
				UiApplication.getUiApplication().pushScreen(new CheckinScreen(fb, user));
				return true;
			}
		};

		setTitle(new LabelField("Loading...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		vfm1 = new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL);
		vfm1.add(tryAgainButton);

		vfm2 = new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL);

		vfm3 = new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL);
		vfm3.add(friendsButton);
		vfm3.add(statusesButton);
		vfm3.add(feedButton);
		vfm3.add(homeButton);
		vfm3.add(checkinsButton);
		vfm3.add(postWallButton);
		vfm3.add(uploadPhotosButton);
		vfm3.add(checkinButton);

		showUserAsync(pUserId);
	}

	public void showUserSync(String pUserId) {
		try {
			user = fb.getUser(pUserId);
			setTitle(new LabelField("Hello " + user.getName() + " !", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
			vfm2.deleteAll();
			vfm2.add(new LabelField("id = " + user.getId()));
			vfm2.add(new LabelField("name = " + user.getName()));
			vfm2.add(new LabelField("email = " + user.getEmail()));
			vfm2.add(new LabelField("birthday = " + user.getBirthdayAsString()));
			vfm2.add(new LabelField("updated_time = " + user.getUpdatedTime()));
			add(vfm2);
			add(vfm3);

		} catch (Exception e) {
			e.printStackTrace();
			setTitle(new LabelField("Error Encountered", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
			vfm2.deleteAll();
			vfm2.add(new LabelField("Exception: " + e.getMessage()));
			add(vfm1);
			add(vfm2);

		} finally {
		}
	}

	public void showUserAsync(String pUserId) {
		try {
			fb.getUser(pUserId, new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					user = (User) objects[0];
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Hello " + user.getName() + " !", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
							vfm2.deleteAll();
							vfm2.add(new LabelField("id = " + user.getId()));
							vfm2.add(new LabelField("name = " + user.getName()));
							vfm2.add(new LabelField("email = " + user.getEmail()));
							vfm2.add(new LabelField("birthday = " + user.getBirthdayAsString()));
							vfm2.add(new LabelField("updated_time = " + user.getUpdatedTime()));
						}
					});
				}

				public void onException(final Exception e, final java.lang.Object state) {
					e.printStackTrace();
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Error Encountered", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
							vfm2.deleteAll();
							vfm2.add(new LabelField("Exception: " + e.getMessage()));
							add(vfm1);
							delete(vfm3);
						}
					});
				}

			}, null);

		} catch (FacebookException e) {
			e.printStackTrace();
		}

		setTitle(new LabelField("Retrieving...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
		vfm2.deleteAll();
		vfm2.add(new LabelField("id = ..."));
		vfm2.add(new LabelField("name = ..."));
		vfm2.add(new LabelField("email = ..."));
		vfm2.add(new LabelField("birthday = ..."));
		vfm2.add(new LabelField("updated_time = ..."));
		add(vfm2);
		add(vfm3);

	}

	protected boolean onSavePrompt() {
		return true;
	}

}
