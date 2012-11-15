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
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.User;

public class PostWallScreen extends MainScreen {

	protected Facebook fb;
	protected User currentUser;
	protected Profile[] users = null;
	protected String[] userNames = null;
	protected ObjectChoiceField usersCF;
	protected EditField messageEditField;
	protected EditField linkEditField;
	protected EditField pictureEditField;
	protected EditField nameEditField;
	protected EditField captionEditField;
	protected EditField descriptionEditField;
	protected EditField sourceEditField;
	protected ButtonField postButtonField;

	public PostWallScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;
		currentUser = pUser;

		setTitle(new LabelField("Retrieving...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		usersCF = new ObjectChoiceField();
		usersCF.setLabel("Post to wall of: ");
		add(usersCF);

		add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));

		messageEditField = new EditField("Message: ", "Check out the new Facebook BlackBerry SDK!");
		add(messageEditField);

		linkEditField = new EditField("Link: ", "http://sf.net/projects/facebook-bb-sdk/");
		add(linkEditField);

		pictureEditField = new EditField("Picture: ", "https://a.fsdn.com/con/icons/fa/facebook-bb-sdk@sf.net/bb_logo_48x48.png");
		add(pictureEditField);

		nameEditField = new EditField("Name: ", "Facebook BlackBerry SDK");
		add(nameEditField);

		captionEditField = new EditField("Caption: ", "http://sf.net/projects/facebook-bb-sdk/");
		add(captionEditField);

		descriptionEditField = new EditField("Description: ", "A Java library for your BlackBerry applications to integrate with Facebook using OAuth 2.0 and Graph API.");
		add(descriptionEditField);

		sourceEditField = new EditField("Source: ", "");
		add(sourceEditField);

		postButtonField = new ButtonField("Post") {
			protected boolean invokeAction(int action) {
				if (users != null) {
					publishNewPost();
				}
				return true;
			}
		};
		add(postButtonField);
		loadList();
	}

	protected void publishNewPost() {
		String result = null;
		if (usersCF.getSelectedIndex() == 0) {
			result = currentUser.publishPost(messageEditField.getText(), linkEditField.getText(), pictureEditField.getText(), nameEditField.getText(), captionEditField.getText(), descriptionEditField.getText(), sourceEditField.getText());
		} else {
			User target = (User) users[usersCF.getSelectedIndex() - 1].toObject();
			target.publishPost(messageEditField.getText(), linkEditField.getText(), pictureEditField.getText(), nameEditField.getText(), captionEditField.getText(), descriptionEditField.getText(), sourceEditField.getText());
		}
		if ((result != null) && !result.trim().equals("")) {
			Dialog.inform("Publish Success.");
		} else {
			Dialog.inform("Publish Failed.");
		}
	}

	void loadList() {
		try {
			currentUser.getFriends(new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					users = (Profile[]) objects;
					if ((users == null) || (users.length <= 0)) {
						userNames = new String[1];
						userNames[0] = currentUser.getName();
					} else {
						userNames = new String[users.length + 1];
						userNames[0] = currentUser.getName();
						for (int i = 1; i < (users.length + 1); i++) {
							userNames[i] = users[i - 1].getName();
						}
					}
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Post To Wall", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
							usersCF.setChoices(userNames);
						}
					});
				}

				public void onException(final Exception e, final java.lang.Object state) {
					e.printStackTrace();
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Error Encountered", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
						}
					});
				}

			}, null);

		} catch (Exception e) {
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}

}
