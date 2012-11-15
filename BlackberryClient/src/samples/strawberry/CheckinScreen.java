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
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.inf.User;

public class CheckinScreen extends MainScreen {

	protected Facebook fb;
	protected User currentUser;
	protected EditField messageEditField;
	protected EditField placeEditField;
	protected EditField latitudeEditField;
	protected EditField longitudeEditField;
	protected ButtonField checkinButtonField;

	public CheckinScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;
		currentUser = pUser;

		setTitle(new LabelField("Checkin", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		messageEditField = new EditField("Message: ", "I am visiting RIM...");
		add(messageEditField);

		placeEditField = new EditField("Place: ", "100760929997256");
		add(placeEditField);

		latitudeEditField = new EditField("Latitude: ", "22.28848");
		add(latitudeEditField);

		longitudeEditField = new EditField("Longitude: ", "114.21621");
		add(longitudeEditField);

		checkinButtonField = new ButtonField("Checkin") {
			protected boolean invokeAction(int action) {
				currentUser.checkin(messageEditField.getText(), placeEditField.getText(), Double.parseDouble(latitudeEditField.getText()), Double.parseDouble(longitudeEditField.getText()), null, new BasicAsyncCallback() {

					public void onComplete(String[] values, final java.lang.Object state) {
						final String result = values[0];
						if ((result != null) && !result.trim().equals("")) {
							UiApplication.getApplication().invokeLater(new Runnable() {
								public void run() {
									Dialog.inform("Checkin ID: " + result);
								}
							});
						} else {
							UiApplication.getApplication().invokeLater(new Runnable() {
								public void run() {
									Dialog.inform("Checkin Failed.");
								}
							});
						}
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

				return true;
			}
		};
		add(checkinButtonField);
	}

	protected boolean onSavePrompt() {
		return true;
	}

}
