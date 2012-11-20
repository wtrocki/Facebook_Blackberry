/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package samples.strawberry;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.blackberry.facebook.AsyncCallbackAdapter;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.model.IUser;

public class ShowUserScreen extends MainScreen {

	protected ButtonField tryAgainButton = null;
	protected VerticalFieldManager vfm2 = null;
	protected Facebook fb;
	protected IUser user;

	public ShowUserScreen(Facebook pfb) {
		this(pfb, "me");
	}

	public ShowUserScreen(Facebook pfb, final String pUserId) {
		super();
		fb = pfb;

		tryAgainButton = new ButtonField("Try Again") {
			protected boolean invokeAction(int action) {
				delete(vfm2);
				showUserSync(pUserId);
				return true;
			}
		};

		setTitle(new LabelField("Loading...", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH));

		vfm2 = new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL);
		showUserAsync(pUserId);
	}

	public void showUserSync(String pUserId) {
		try {
			user = fb.getUser(pUserId);
			setTitle(new LabelField("Hello " + user.getName() + " !",
					LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
			vfm2.deleteAll();
			vfm2.add(new LabelField("id = " + user.getId()));
			vfm2.add(new LabelField("name = " + user.getName()));
			vfm2.add(new LabelField("email = " + user.getEmail()));
			vfm2.add(new LabelField("birthday = " + user.getBirthdayAsString()));
			vfm2.add(new LabelField("updated_time = "
					+ user.getUpdatedTimeAsString()));
			add(vfm2);

		} catch (Exception e) {
			e.printStackTrace();
			setTitle(new LabelField("Error Encountered", LabelField.ELLIPSIS
					| LabelField.USE_ALL_WIDTH));
			vfm2.deleteAll();
			vfm2.add(new LabelField("Exception: " + e.getMessage()));
			add(vfm2);

		} finally {
		}
	}

	public void showUserAsync(String pUserId) {
		try {
			fb.getUser(pUserId, new AsyncCallbackAdapter() {

				public void onComplete(
						com.blackberry.facebook.model.IObject[] objects,
						final java.lang.Object state) {
					user = (IUser) objects[0];
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Hello " + user.getName()
									+ " !", LabelField.ELLIPSIS
									| LabelField.USE_ALL_WIDTH));
							vfm2.deleteAll();
							vfm2.add(new LabelField("id = " + user.getId()));
							vfm2.add(new LabelField("name = " + user.getName()));
							vfm2.add(new LabelField("email = "
									+ user.getEmail()));
							vfm2.add(new LabelField("birthday = "
									+ user.getBirthdayAsString()));
							vfm2.add(new LabelField("updated_time = "
									+ user.getUpdatedTimeAsString()));
						}
					});
				}

				public void onException(final Exception e,
						final java.lang.Object state) {
					e.printStackTrace();
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Error Encountered",
									LabelField.ELLIPSIS
											| LabelField.USE_ALL_WIDTH));
							vfm2.deleteAll();
							vfm2.add(new LabelField("Exception: "
									+ e.getMessage()));
						}
					});
				}
			}, null);

		} catch (FacebookException e) {
			e.printStackTrace();
		}

		setTitle(new LabelField("Retrieving...", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH));
		vfm2.deleteAll();
		vfm2.add(new LabelField("id = ..."));
		vfm2.add(new LabelField("name = ..."));
		vfm2.add(new LabelField("email = ..."));
		vfm2.add(new LabelField("birthday = ..."));
		vfm2.add(new LabelField("updated_time = ..."));
		add(vfm2);
	}

	protected boolean onSavePrompt() {
		return true;
	}

}
