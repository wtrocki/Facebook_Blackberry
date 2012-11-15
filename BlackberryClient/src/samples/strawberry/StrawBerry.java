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

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.ApplicationSettings;
import com.blackberry.facebook.Facebook;

public class StrawBerry extends UiApplication {

	private final String NEXT_URL = "http://www.facebook.com/connect/login_success.html";
	private final String APPLICATION_ID = "153555168010272";
	private final String APPLICATION_SECRET = "354f91a79c8fe5a8de9d65b55ef9aa1b";
	private Facebook fb;
	private Facebook fb4u;

	public static void main(String[] args) {
		new StrawBerry().enterEventDispatcher();
	}

	public StrawBerry() {
		checkPermissions();
		fb = Facebook.getInstance(new ApplicationSettings(NEXT_URL, APPLICATION_ID, APPLICATION_SECRET, Facebook.Permissions.ALL_PERMISSIONS));
		fb4u = Facebook.getInstance(new ApplicationSettings(NEXT_URL, APPLICATION_ID, APPLICATION_SECRET, Facebook.Permissions.USER_DATA_PERMISSIONS));
		fb4u.addPermission(Facebook.Permissions.OFFLINE_ACCESS);
		pushScreen(new HomeScreen());
	}

	private void checkPermissions() {
		ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
		ApplicationPermissions original = apm.getApplicationPermissions();
		if ((original.getPermission(ApplicationPermissions.PERMISSION_MEDIA) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_RECORDING) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_EMAIL) == ApplicationPermissions.VALUE_ALLOW)) {
			return;
		}
		ApplicationPermissions permRequest = new ApplicationPermissions();
		permRequest.addPermission(ApplicationPermissions.PERMISSION_MEDIA);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_RECORDING);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_EMAIL);

		boolean acceptance = ApplicationPermissionsManager.getInstance().invokePermissionsRequest(permRequest);

		if (acceptance) {
			return;
		} else {
		}
	}

	public class HomeScreen extends MainScreen {

		protected ButtonField showUserButton = null;
		protected ButtonField unitTestsButton = null;
		protected ButtonField logoutButton = null;

		public HomeScreen() {
			super();

			showUserButton = new ButtonField("Show Current User") {
				protected boolean invokeAction(int action) {
					UiApplication.getUiApplication().pushScreen(new ShowUserScreen(fb));
					return true;
				}
			};

			unitTestsButton = new ButtonField("Unit Tests") {
				protected boolean invokeAction(int action) {
					UiApplication.getUiApplication().pushScreen(new UnitTestsScreen(fb4u));
					return true;
				}
			};

			logoutButton = new ButtonField("Logout") {
				protected boolean invokeAction(int action) {
					if (fb.logout()) {
						Dialog.inform("Logout Success.");
					} else {
						Dialog.inform("Logout Failed.");
					}
					return true;
				}
			};

			setTitle(new LabelField("StrawBerry", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
			add(showUserButton);
			add(unitTestsButton);
			add(logoutButton);

		}

		protected boolean onSavePrompt() {
			return true;
		}

	}

}