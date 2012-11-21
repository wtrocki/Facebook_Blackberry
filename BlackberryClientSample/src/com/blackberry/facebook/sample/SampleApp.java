/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.sample;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.ApplicationSettings;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.Permissions;

public class SampleApp extends UiApplication {

	private final String NEXT_URL = "http://www.facebook.com/connect/login_success.html";
	private final String APPLICATION_ID = "153555168010272";
	private final String APPLICATION_SECRET = "354f91a79c8fe5a8de9d65b55ef9aa1b";
	private Facebook fb4u;

	public static void main(String[] args) {
		SampleApp strawBerry = new SampleApp();
		strawBerry.enterEventDispatcher();
	}

	public SampleApp() {
		checkPermissions();
		fb4u = Facebook.getInstance(new ApplicationSettings(NEXT_URL,
				APPLICATION_ID, APPLICATION_SECRET,
				Permissions.USER_DATA_PERMISSIONS));
		fb4u.addPermission(Permissions.OFFLINE_ACCESS);
		pushScreen(new HomeScreen());
	}

	private void checkPermissions() {
		ApplicationPermissionsManager apm = ApplicationPermissionsManager
				.getInstance();
		ApplicationPermissions original = apm.getApplicationPermissions();
		if ((original.getPermission(ApplicationPermissions.PERMISSION_MEDIA) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_RECORDING) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK) == ApplicationPermissions.VALUE_ALLOW)
				&& (original
						.getPermission(ApplicationPermissions.PERMISSION_EMAIL) == ApplicationPermissions.VALUE_ALLOW)) {
			return;
		}
		ApplicationPermissions permRequest = new ApplicationPermissions();
		permRequest.addPermission(ApplicationPermissions.PERMISSION_MEDIA);
		permRequest
				.addPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_RECORDING);
		permRequest
				.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		permRequest
				.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);
		permRequest
				.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
		permRequest
				.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_EMAIL);

		boolean acceptance = ApplicationPermissionsManager.getInstance()
				.invokePermissionsRequest(permRequest);

		if (acceptance) {
			return;
		} else {
		}
	}

	public class HomeScreen extends MainScreen {

		protected ButtonField showUserButton = null;

		public HomeScreen() {
			super();

			showUserButton = new ButtonField("Show User") {
				protected boolean invokeAction(int action) {
					UiApplication.getUiApplication().pushScreen(
							new ShowUserScreen(fb4u));
					return true;
				}
			};
			add(showUserButton);
			setTitle(new LabelField("Facebook api sample", LabelField.ELLIPSIS
					| LabelField.USE_ALL_WIDTH));

		}
	}

}