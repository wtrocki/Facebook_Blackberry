/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package samples.strawberry;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.User;

public class ShowHomeScreen extends MainScreen {

	protected Facebook fb;
	protected User user = null;

	public ShowHomeScreen(Facebook pfb) throws FacebookException {
		this(pfb, "me");
	}

	public ShowHomeScreen(Facebook pfb, String pUserId)
			throws FacebookException {
		this(pfb, pfb.getUser(pUserId));
	}

	public ShowHomeScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;
		user = pUser;
		if (user != null) {
			setTitle(new LabelField("Home of " + user.getName(),
					LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
		} else {
			setTitle(new LabelField("Retrieving...", LabelField.ELLIPSIS
					| LabelField.USE_ALL_WIDTH));
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}

}